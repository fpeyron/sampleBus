package fr.boursorama.bus.mock.ttis;

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

        from("cxf:bean:ttis.SMPCardServices")
                .to("log:ttis.SMPCardServices.input?level=DEBUG&showBody=true")
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .to("validator:xsd/CSD002Aller.xsd")
                .to(ExchangePattern.InOnly, "seda:response")
                .to("xslt:xsl/ttisMock.xsl")
                .to("log:ttis.SMPCardServices.output?level=DEBUG&showBody=true")
        ;

        from("seda:response")
                .to("xslt:xsl/brsMock.xsl")
                .removeHeaders("*", "callback.*")
                .choice()
                    .when(simple("${header.debug.url}"))
                    .log(LoggingLevel.DEBUG, "${header.debug.url}")
                    .setHeader(Exchange.DESTINATION_OVERRIDE_URL).simple("${in.header.debug.url}")
                    .endChoice()
                    .otherwise()
                .end()
                .choice()
                    .when(simple("${header.debug.sleep}"))
                    .delay().simple("${in.header.debug.sleep}")
                    .endChoice()
                    .otherwise()
                .end()
                .to(ExchangePattern.InOut, "cxf:bean:brs.SMPCardServices")
        ;
    }
}
