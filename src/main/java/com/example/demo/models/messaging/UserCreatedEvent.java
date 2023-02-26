package com.example.demo.models.messaging;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreatedEvent implements Event {
	
	private UUID eventId = UUID.randomUUID();
	
	private String oktaUserId;

	private String firstName;

	private String lastName;

	private String emailId;

	private UserStatus userStatus;
	
	private Date date = new Date();

	@Override
	public UUID getEventId() {
		return eventId;
	}

	@Override
	public Date getDate() {
		return date;
	}

}
