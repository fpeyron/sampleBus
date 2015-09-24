package fr.boursorama.bus.util.broker;

/**
 * Created by florent on 31/08/15.
 */
public class BrokerUtil {

    public static String consumer(String exchangeName) {

        return String.format("rabbitmq://host/%s" +
                "?connectionFactory=#customConnectionFactory" +
                "&queue=%s", exchangeName, exchangeName);
    }

    public static String producer(String exchangeName) {

        return String.format("rabbitmq://host/%s" +
                "?connectionFactory=#customConnectionFactory" +
                "&queue=%s", exchangeName, exchangeName);
    }

}