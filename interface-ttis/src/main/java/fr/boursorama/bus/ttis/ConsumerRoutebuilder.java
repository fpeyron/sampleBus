package fr.boursorama.bus.ttis;

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
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .to("validator:xsd/CSD002Retour.xsd")
                //.to("xslt:xsl/brsMock.xsl")
                //.convertBodyTo(String.class)
                .transform().simple("<gen:notifierResponse xmlns:gen=\"http://generic.ttis.bus.boursorama.fr/\"><messageSMPRetourXML /></gen:notifierResponse>")
                .to("log:ttis.SMPCardServices.output?level=INFO&showBody=true")
        ;

    }
}
