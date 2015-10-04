package fr.boursorama.bus.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by florent on 02/10/15.
 */


@Configuration
public class RabbitmqConfig {

    @Value("${rabbitmq.host:localhost}")
    private String rabbitmqHost;

    @Value("${rabbitmq.port:5672}")
    private Integer rabbitmqPort;

    @Value("${rabbitmq.username:guest}")
    private String rabbitmqUsername;

    @Value("${rabbitmq.password:guest}")
    private String rabbitmqPassword;

    @Value("${rabbitmq.virtualHost:/}")
    private String rabbitmqVirtualhost;

    /**
     * Swagger Camel Configuration
     */
    @Bean
    public ConnectionFactory customConnectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitmqHost);
        connectionFactory.setPort(rabbitmqPort);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        connectionFactory.setVirtualHost(rabbitmqVirtualhost);
        connectionFactory.setAutomaticRecoveryEnabled(true);

        return connectionFactory;
    }

}
