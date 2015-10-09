package fr.boursorama.bus.ttis;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;


/**
 * @author fpeyron
 */
@Component
public class TtisConsumerRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(BrokerUtil.consumer("ttis.service"))
                .errorHandler(deadLetterChannel(BrokerUtil.producer("ttis.service.fail")))
                .removeHeaders("*", "debug.*")
                .to(ExchangePattern.InOut, BrokerUtil.producer("ttis.consumer"))
                .log(LoggingLevel.INFO, "<<< ${body}")
        ;

        from("rabbitmq://localhost/ttis.consumer?queue=ttis.consumer")
                .to("log:bus.interface.ttis.SMPCardServices.input?level=DEBUG&showBody=true")
                .log(LoggingLevel.INFO, "<< ${body}")
                .enrich("direct:getPan", new AggregationStrategy() {
                    @Override
                    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                        oldExchange.getIn().setHeader("pan", newExchange.getIn().getHeader("pan"));
                        return oldExchange;
                    }
                })
                .log(LoggingLevel.INFO, "pan: ${header.pan}")
                .log(LoggingLevel.INFO, "<< ${header.rabbitmq.REPLY_TO} ${header.rabbitmq.CORRELATIONID}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        if (exchange.getIn().getHeader(RabbitMQConstants.REPLY_TO) != null) {
                            exchange.getIn().setHeader("correlationId"
                                    , String.format("%s;%s"
                                    , exchange.getIn().getHeader(RabbitMQConstants.REPLY_TO, String.class)
                                    , exchange.getIn().getHeader(RabbitMQConstants.CORRELATIONID, String.class)
                            ));
                        } else {
                            exchange.getIn().setHeader("correlationId", exchange.getIn().getMessageId());
                        }
                    }
                })
                .to("xslt:fr/boursorama/bus/ttis/xsl/internal2ttis.xsl")
                .to("validator:fr/boursorama/bus/ttis/xsd/CSD002Aller.xsd")
                .setBody().simple("<gen:activer xmlns:gen=\"http://generic.monetiq.evolan.sopra.com/\"><messageSMPAllerXML>${body}</messageSMPAllerXML></gen:activer>")
                //.setHeader("callback.sleep").constant(3000)
                .setHeader("debug.url").constant("http://localhost:7080/boursorama-bus-service/soap/brs.SMPCardServices")
                .removeHeaders("*", "debug.*")
                //.setHeader(CxfConstants.OPERATION_NAMESPACE).constant("http://generic.monetiq.evolan.sopra.com/")
                //.setHeader(CxfConstants.OPERATION_NAME).constant("activer")
                .log(LoggingLevel.INFO, ">>${header." + CxfConstants.OPERATION_NAME + "}")
                .to("cxf:bean:ttis.SMPCardServices")
                .to("log:bus.interface.ttis.SMPCardServices.output?level=DEBUG&showBody=true")
                .log(LoggingLevel.INFO, "<< ${body}")
        ;


        from("direct:getPan")
                .setBody().constant(null)
                .setHeader("pan").constant("1234567890123")
        ;
    }
}
