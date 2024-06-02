package com.example.rabbitmq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbitmq.service.UserService;

@RestController
@RequestMapping("user")
public class UserControllerImpl implements UserController {

	private UserService userService;
	
	public UserControllerImpl(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	@GetMapping("sendqueueuser/{userId}")
	public void sendQueueUser(@PathVariable String userId, @RequestParam(defaultValue = "#") String routingKey) {
		userService.sendQueueUser(userId, routingKey);
	}
	
}
