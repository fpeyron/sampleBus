package fr.boursorama.bus.mock.ttis;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author fpeyron
 */
@Component
public class ConsumerRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("cxf:bean:ttis.SMPCardServices")
                .to("log:ttis.SMPCardServices.input?level=INFO&showBody=true&showHeaders=true")
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .to("validator:xsd/CSD002Aller.xsd")
                .to("xslt:xsl/ttisMock.xsl")
                .to("log:ttis.SMPCardServices.output?level=INFO&showBody=true")
        ;

    }
}
