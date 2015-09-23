package fr.boursorama.bus.partner;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


/**
 * @author fpeyron
 */
@Component
public class ConsumerRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("cxf:bean:brs.SMPCardServices")
                //.to("log:ttis.SMPCardServices.input?level=INFO&showBody=true")
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .to("validator:xsd/CSD002Retour.xsd")
                .to("xslt:xsl/brsMock.xsl")
                .to("log:ttis.SMPCardServices.output?level=INFO&showBody=true")
        ;

    }
}
