package fr.boursorama.bus.ttis;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
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
                .to("log:bus.interface.ttis.SMPBusServices.input?level=INFO&showBody=true&showHeaders=true")
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .setHeader("messageType").xpath("local-name(/*[1])", String.class)
                .setHeader("messageVersion").xpath("/*[1]/serviceMetierPublicRetour/version/text()", String.class)
                .setHeader(RabbitMQConstants.CORRELATIONID).xpath("/*[1]/serviceMetierPublicRetour/refExtDemSMP/text()")
                .choice()
                .when().simple("${in.header.messageType} == 'CSD002Retour'")
                .to("validator:fr/boursorama/bus/ttis/xsd/CSD002Retour.xsd")
                .endChoice()
                .otherwise()
                .throwException(new Exception("messageType is unknow"))
                .end()
                        //.to("xslt:xsl/brsMock.xsl")
                        //.convertBodyTo(String.class)
                .log(LoggingLevel.INFO, "${header.messageType} : ${header.rabbitmq.CORRELATIONID}")
                .to(ExchangePattern.InOnly, BrokerUtil.producer("ttis.producer"))
                .transform().constant("<gen:notifierResponse xmlns:gen=\"http://generic.ttis.bus.boursorama.fr/\"><messageSMPRetourXML /></gen:notifierResponse>")
                .removeHeaders("*")
                .to("log:bus.interface.SMPBusServices.output?level=INFO&showBody=true")
        ;

    }
}
