package fr.boursorama.camel.rabbitmq;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;


/**
 * Created by Florent Peyron on 13/06/15.
 * patch https://issues.apache.org/jira/browse/CAMEL-7860
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {CamelRabbitmq2Test.TestConfig.class},
        loader = CamelSpringDelegatingTestContextLoader.class
)
@MockEndpoints
@org.junit.Ignore
public class CamelRabbitmq2Test {

    @Produce(uri = "direct:testRabbitmq")
    protected ProducerTemplate testProducer;

    @Test
    public void testRequestReplyRoute() throws InterruptedException {

        Exchange response = testProducer.send("direct:testRabbitmq", new Processor() {
            //@Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("<request>mon test</request>");
            }
        });
        Assert.assertEquals("<result>confirmed</result>", response.getIn().getBody());

    }


    public static class TestConfig extends SingleRouteCamelConfiguration {
        @Bean
        @Override
        public RouteBuilder route() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {

                    from("direct:testRabbitmq")
                            .log(LoggingLevel.INFO, "request : ${body}")
                            //.setHeader(RabbitMQConstants.REPLY_TO, constant("serviceReply"))
                            .to(ExchangePattern.InOut, "rabbitmq://localhost/serviceRequest?queue=serviceRequest")
                            .convertBodyTo(String.class)
                            .log(LoggingLevel.INFO, "response : ${body}")
                    ;

                    from("rabbitmq://localhost/serviceRequest?queue=serviceRequest")
                            .log(LoggingLevel.INFO, "request : ${body}")
                                    //.delayer(5000)
                            .setBody(constant("<result>ok</result>"))
                            .setHeader("REPLY_TO").simple("${header.rabbitmq.REPLY_TO}")
                            .setHeader("CORRELATIONID").simple("${header.rabbitmq.CORRELATIONID}")
                            .removeHeaders("rabbitmq.*")
                            .to("direct:result")
                    ;

                    from("direct:result")
                            .setBody(constant("<result>confirmed</result>"))
                            .setHeader("rabbitmq.ROUTING_KEY").simple("${header.REPLY_TO}")
                            .setHeader("rabbitmq.CORRELATIONID").simple("${header.CORRELATIONID}")
                            //.delay(2000)
                            .to(ExchangePattern.InOnly, "rabbitmq://localhost/serviceRequest")
                    ;

                }
            };
        }

    }
}
