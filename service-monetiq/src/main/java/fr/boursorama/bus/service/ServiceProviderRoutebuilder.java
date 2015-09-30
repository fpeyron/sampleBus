package fr.boursorama.bus.service;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by florent on 25/09/15.
 */
@Component
public class ServiceProviderRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration().component("servlet")
                //.bindingMode(RestBindingMode.xml)
                .dataFormatProperty("prettyPrint", "true")
        ;

        rest("/test")
                .get("/hello")
                .description("A dummy call")
                .to("direct:helloService")
                .post("/ttis")
                .description("test ttis")
                .to("direct:callTtis")
        ;


        from("direct:helloService")
                .log(LoggingLevel.DEBUG, "request Hello")
                .setBody(constant("bye"))
        ;

        from("direct:callTtis")
                .removeHeaders("*", "debug.*")
                .log(LoggingLevel.INFO, ">>> ${body}")
                .to(ExchangePattern.InOut, BrokerUtil.producer("ttis.consumer"))
                .log(LoggingLevel.INFO, "<<< ${body}")
        ;

    }
}

