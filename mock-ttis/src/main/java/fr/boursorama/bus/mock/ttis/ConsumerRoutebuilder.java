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
                .to("log:ttis.SMPCardServices.input?level=INFO&showBody=true")
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .to("validator:xsd/CSD002Aller.xsd")
                .to(ExchangePattern.InOnly, "seda:response")
                .to("xslt:xsl/ttisMock.xsl")
                //.transform().constant("<gen:activerResponse xmlns:gen=\"http://generic.monetiq.evolan.sopra.com/\"><messageSMPRetourXML /></gen:activerResponse>")
                .to("log:ttis.SMPCardServices.output?level=INFO&showBody=true")
        ;

        from("seda:response")
                .to("xslt:xsl/brsMock.xsl")
                .removeHeaders("*", "callback.*")
                .choice()
                    .when(simple("${header.callback.url}"))
                    .setHeader(Exchange.DESTINATION_OVERRIDE_URL).simple("${in.header.callback.url}")
                    .endChoice()
                    .otherwise()
                .end()
                .choice()
                    .when(simple("${header.callback.sleep}"))
                    .delay().simple("${in.header.callback.sleep}")
                    .endChoice()
                    .otherwise()
                .end()
                .to(ExchangePattern.InOut, "cxf:bean:brs.SMPCardServices")
        ;
    }
}
