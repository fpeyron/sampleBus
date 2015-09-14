package fr.sysf.bus.util.broker;

/**
 * Created by florent on 31/08/15.
 */
public class BrokerUtil {

    public static String consumer(String exchangeName) {

        return String.format("rabbitmq://host/%s" +
                "?connectionFactory=#customConnectionFactory" +
                "&queue=%s", exchangeName, exchangeName);
    }
}