package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.models.messaging.UserCreatedEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;


@Configuration
public class UserEventConfig {
	
	@Bean
	public Sinks.Many<UserCreatedEvent> createUserEvSink() {
		return Sinks.many().multicast().onBackpressureBuffer();
	}
	
	@Bean
	public Supplier<Flux<UserCreatedEvent>> userCreateSupplier(Sinks.Many<UserCreatedEvent> createUserEvSink){
		return createUserEvSink :: asFlux;
		
	}
}
