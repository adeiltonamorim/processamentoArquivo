package com.amorim.reader.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amorim.reader.message.ProcessorMessage;

@Component
public class ProcessorSendMessage {

	@Value("${processor.rabbitmq.exchange}")
	private String exchange;

	@Value("${processor.rabbitmq.routingkey}")
	private String routingkey;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

    public void sendMessage(ProcessorMessage payload) {
        System.out.println(payload);
        System.out.println(exchange);
        System.out.println(routingkey);
        rabbitTemplate.convertAndSend(exchange, routingkey, payload);
    }
    
}