package io.pivotal.edu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by danielbuchko on 2017-04-12.
 */

@RestController
public class DemoController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RabbitTemplate rabbitTemplate;
    private final String queue;

    public DemoController(ConnectionFactory connectionFactory, @Value("${rabbitmq.queue}") String queue) {
        this.rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.queue = queue;
    }

    @RequestMapping("/publish")
    public void publish() {
        rabbitTemplate.convertAndSend(queue, "Greetings!");

        logger.debug("Sent a message!");
        return;
    }

}
