package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserDto;
import com.example.demo.models.messaging.UserCreatedEvent;
import com.example.demo.models.messaging.UserStatus;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.UserBuilder;

import reactor.core.publisher.Sinks;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.User;

@Service
public class UserService {

	@Autowired
	Sinks.Many<UserCreatedEvent> userEvSink;

	@Transactional
	public User createUser(UserDto user) throws Exception {

		UserApi userApi = new UserApi(getClient());
		User oktaUser = null;
		try {
			oktaUser = UserBuilder.instance().setEmail(user.getEmail()).setFirstName(user.getFirstName())
					.setLastName(user.getLastName()).setPassword(user.getPassword().toCharArray()).setActive(true)
					.buildAndCreate(userApi);
			UserCreatedEvent event = new UserCreatedEvent();
			event.setOktaUserId(oktaUser.getId());
			event.setEmailId(user.getEmail());
			event.setFirstName(user.getFirstName());
			event.setLastName(user.getLastName());
			event.setUserStatus(UserStatus.ACTIVE);

			userEvSink.tryEmitNext(event);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return oktaUser;

	}

	private ApiClient getClient() {
		return Clients.builder().setOrgUrl("https://dev-63954939.okta.com")
				.setClientCredentials(new TokenClientCredentials("00SNIja9jeDl6ZG8veBwfQnoMdxFCLiNZ84biFIgFB")).build();
	}

	public boolean deleteUser(String userId) {
		UserApi userApi = new UserApi(getClient());
		userApi.deactivateUser(userId, false);
		userApi.deleteUser(userId, null);
		return true;
	}
}
