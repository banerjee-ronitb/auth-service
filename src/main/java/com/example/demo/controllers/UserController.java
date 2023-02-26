package com.example.demo.controllers;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.UserDto;
import com.example.demo.services.UserService;
import org.openapitools.client.model.User;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:8081")
public class UserController {

	public final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	OAuth2AuthorizedClientService clientService;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	@PreAuthorize("hasAuthority('SCOPE_profile')")
	public String init(@AuthenticationPrincipal OidcUser principal, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

		String accessToken = this.getAuthorizedClient(oauthToken).getAccessToken().getTokenValue();
		String idToken = principal.getIdToken().getTokenValue();

		Cookie idTokenCookie = new Cookie("NikeUserToken", idToken);
		idTokenCookie.setPath("/");
		idTokenCookie.setSecure(true);
		idTokenCookie.setHttpOnly(true);

		Cookie accessTokenCookie = new Cookie("NikeAccessToken", accessToken);
		accessTokenCookie.setPath("/");
		accessTokenCookie.setSecure(true);
		accessTokenCookie.setHttpOnly(true);
		response.addCookie(idTokenCookie);
		response.addCookie(accessTokenCookie);
		return accessToken;

	}

	// For JWT only

	@GetMapping("/api/userinfo")
	@PreAuthorize("hasAuthority('SCOPE_profile')")
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserDetails(@AuthenticationPrincipal OidcUser principal,
			HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

		String userInfoEndpointUri = this.getAuthorizedClient(oauthToken).getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUri();

		Map<String, Object> userAttributes = (Map<String, Object>) WebClient.builder()
				.filter(oauth2Credentials(this.getAuthorizedClient(oauthToken))).build().get().uri(userInfoEndpointUri)
				.retrieve().bodyToMono(Map.class).block();

		return userAttributes;

	}

	@PostMapping("/user")
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) throws Exception {

		log.info("Creating New User");
		User userEntity = userService.createUser(user);
		return new ResponseEntity<UserDto>(convertEntityToDto(userEntity), HttpStatus.CREATED);

	}

	@DeleteMapping("/user/{userId}")	
	public BodyBuilder deleteUser(@PathVariable("userId") String userId) {
		log.info(userId);
		return Optional.ofNullable(userService).map(service -> service.deleteUser(userId)).map(val -> {
			if (val) {
				return ResponseEntity.ok();
			} else
				return ResponseEntity.badRequest();
		}).orElse(null);
	}

	private UserDto convertEntityToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getProfile().getEmail());
		userDto.setFirstName(user.getProfile().getFirstName());
		userDto.setLastName(user.getProfile().getLastName());
		userDto.setId(user.getId());
		return userDto;
	}

	private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
		return this.clientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
				authentication.getName());
	}

	private ExchangeFilterFunction oauth2Credentials(OAuth2AuthorizedClient authorizedClient) {
		return ExchangeFilterFunction.ofRequestProcessor(clientrequest -> {
			return Mono.just(ClientRequest.from(clientrequest)
					.header(HttpHeaders.AUTHORIZATION, "Bearer" + authorizedClient.getAccessToken().getTokenValue())
					.build());

		});
	}

}
