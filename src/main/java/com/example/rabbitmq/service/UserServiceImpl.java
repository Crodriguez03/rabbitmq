package com.example.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.rabbitmq.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {
	
	@Value("${rabbitmq.exchange.user}")
	private String exchangeUser;
	
	private final RabbitTemplate rabbitTemplate;
	
	public UserServiceImpl(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	@Override
	public void sendQueueUser(String userId, String routingKey) {
		UserDTO user = new UserDTO(userId, "name", "surname");
		rabbitTemplate.convertAndSend(exchangeUser, routingKey, user);
	}
}
