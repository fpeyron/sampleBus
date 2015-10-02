package fr.boursorama.bus.ttis;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.springframework.stereotype.Component;


/**
 * @author fpeyron
 */
@Component
public class TtisProviderRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("cxf:bean:brs.SMPCardServices")
                .to("log:bus.interface.ttis.SMPBusServices.input?level=DEBUG&showBody=true&showHeaders=true")
                .log(LoggingLevel.INFO, "<< ${body}")
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .setHeader("messageType").xpath("local-name(/*[1])", String.class)
                .setHeader("messageVersion").xpath("/*[1]/serviceMetierPublicRetour/version/text()", String.class)
                .setHeader("correlationId").xpath("/*[1]/serviceMetierPublicRetour/refExtDemSMP/text()")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        final String[] correlation = exchange.getIn().getHeader("correlationId", String.class).split(";");
                        if (correlation.length > 1) {
                            exchange.getIn().setHeader(RabbitMQConstants.ROUTING_KEY, correlation[0]);
                            exchange.getIn().setHeader(RabbitMQConstants.CORRELATIONID, correlation[1]);
                        }
                    }
                })
                .choice()
                .when().simple("${in.header.messageType} == 'CSD002Retour'")
                .to("validator:fr/boursorama/bus/ttis/xsd/CSD002Retour.xsd")
                .endChoice()
                .otherwise()
                .throwException(new Exception("messageType is unknow"))
                .end()
                //.choice()
                //    .when().simple("${header.rabbitmq.ROUTING_KEY} != null")
                //        .to(ExchangePattern.InOnly, BrokerUtil.producer("ttis.consumer"))
                //        .endChoice()
                //    .otherwise()
                        .to(ExchangePattern.InOnly, BrokerUtil.producer("ttis.producer"))
                .end()
                .transform().constant("<gen:notifierResponse xmlns:gen=\"http://generic.ttis.bus.boursorama.fr/\"><messageSMPRetourXML /></gen:notifierResponse>")
                .removeHeaders("*")
                .to("log:bus.interface.SMPBusServices.output?level=DEBUG&showBody=true")
                .log(LoggingLevel.INFO, ">> ${body}")
        ;

    }
}
