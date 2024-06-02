package com.example.rabbitmq.service;

public interface UserService {

	void sendQueueUser(String userId, String routingKey);
}
