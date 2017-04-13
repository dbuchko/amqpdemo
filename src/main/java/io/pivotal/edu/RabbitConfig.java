package io.pivotal.edu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by danielbuchko on 2017-04-12.
 */
@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.queue}") String rabbitMqQueue;
    @Value("${rabbitmq.uri}") String rabbitMqUri;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory());
        container.setQueueNames(rabbitMqQueue);
        container.setMessageListener(demoListener());
        return container;
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(rabbitMqUri);

        // Declare our queue
        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(new Queue(rabbitMqQueue));

        return connectionFactory;
    }

    @Bean
    public MessageListener demoListener() {
        return new MessageListener() {
            public void onMessage(Message message) {
                logger.debug("received: " + message);
            }
        };
    }


}