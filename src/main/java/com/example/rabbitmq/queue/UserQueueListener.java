package com.example.rabbitmq.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.rabbitmq.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserQueueListener {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	private final ObjectMapper objectMapper;
	
	@Value("${rabbitmq.queue.user.group1}")
	private String queueUserGroup1;
	
	@Value("${rabbitmq.queue.user.group2}")
	private String queueUserGroup2;
	
	private static final String DEATH_QUEUE = "x-first-death-queue";
	
	public UserQueueListener(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@RabbitListener(queues = "${rabbitmq.queue.user.group1}", containerFactory = "prefetchRabbitListenerContainerFactory", concurrency = "5")
	public void handleEventUser(Message message) throws Exception {
		
		String body = new String(message.getBody());
		UserDTO user = objectMapper.readValue(body, UserDTO.class);
		
		if ("5".equals(user.getId())) {
			log.info("Error al consumir de la cola {} con userId: {}, name: {} surname: {}", queueUserGroup1, user.getId(), user.getName(), user.getSurname());
			throw new Exception();
		}
		log.info("Consumido mensaje de la cola {} con userId: {}, name: {} surname: {}", queueUserGroup1, user.getId(), user.getName(), user.getSurname());
	}
	
	@RabbitListener(queues = "${rabbitmq.queue.user.group2}", containerFactory = "prefetchRabbitListenerContainerFactory", concurrency = "5")
	public void handleEventError(Message message) throws JsonProcessingException {
		String body = new String(message.getBody());
		UserDTO user = objectMapper.readValue(body, UserDTO.class);
		log.info("Consumido mensaje de la cola {} con userId: {}, name: {} surname: {}", queueUserGroup2, user.getId(), user.getName(), user.getSurname());
	}
	
	@RabbitListener(queues = "${rabbitmq.queue.dead-letter}")
	public void handleEventDeadLetter(Message message) {
		String queue = message.getMessageProperties().getHeader(DEATH_QUEUE);
		String body = new String(message.getBody());
		log.info("Ha llegado a la deadletter un mensaje que no se ha podido consumir de la cola: {}, con body: {}", queue, body);
	}
}
