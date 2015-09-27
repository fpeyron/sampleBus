package fr.boursorama.bus.ttis;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


/**
 * @author fpeyron
 */
@Component
public class TtisConsumerRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(BrokerUtil.consumer("ttis.consumer"))
                .to("log:bus.interface.ttis.SMPCardServices.input?level=INFO&showBody=true")
                .setHeader("correlationId").simple("${exchangeId}")
                .log(LoggingLevel.INFO, ">>>>>${header.correlationId}> ${headers}")
                .to("xslt:fr/boursorama/bus/ttis/xsl/internal2ttis.xsl")
                .to("validator:fr/boursorama/bus/ttis/xsd/CSD002Aller.xsd")
                .setBody().simple("<gen:activer xmlns:gen=\"http://generic.monetiq.evolan.sopra.com/\"><messageSMPAllerXML>${body}</messageSMPAllerXML></gen:activer>")
                .setHeader("callback.sleep").constant(3000)
                .setHeader("callback.url").constant("http://localhost:8080/boursorama-bus-service/soap/brs.SMPCardServices")
                .removeHeaders("*", "callback.*")
                .to("cxf:bean:ttis.SMPCardServices")
                .to("log:bus.interface.ttis.SMPCardServices.output?level=INFO&showBody=true")
        ;

    }
}
