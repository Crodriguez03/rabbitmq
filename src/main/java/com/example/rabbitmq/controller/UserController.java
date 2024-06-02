package com.example.rabbitmq.controller;

public interface UserController {
	
	void sendQueueUser(String userId, String routingKey);
}
