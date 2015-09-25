package fr.boursorama.bus.service;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by florent on 25/09/15.
 */
@Component
public class ProviderRoutebuilder extends RouteBuilder {

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
        ;


        from("direct:helloService")
                .log(LoggingLevel.DEBUG, "request Hello")
                .setBody(constant("bye"))
        ;


    }
}

