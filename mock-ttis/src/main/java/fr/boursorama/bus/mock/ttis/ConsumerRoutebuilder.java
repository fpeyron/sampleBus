package fr.boursorama.bus.mock.ttis;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import javax.xml.xpath.XPath;


/**
 * @author fpeyron
 */
@Component
public class ConsumerRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("cxf:bean:ttis.SMPCardServices")
                .to("log:ttis.SMPCardServices.input?level=DEBUG&showBody=true")
                .log(LoggingLevel.INFO, "ttis.SMPCardServices", ">> ${body}")
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .choice()
                .when().xpath("/CSD001Aller")
                    .to("validator:xsd/CSD001Aller.xsd")
                    .to(ExchangePattern.InOnly, "seda:response")
                .when().xpath("/CSD002Aller")
                    .to("validator:xsd/CSD002Aller.xsd")
                    .to(ExchangePattern.InOnly, "seda:response")
                .when().xpath("/CSQ004Aller")
                    .to("validator:xsd/CSQ004Aller.xsd")
                .otherwise()
                    .throwException(new Exception("Function not found"))
                .end()
                .to("xslt:xsl/ttisMock.xsl")
                .to("log:ttis.SMPCardServices.output?level=DEBUG&showBody=true")
                .log(LoggingLevel.INFO, "ttis.SMPCardServices", "<< ${body}")
        ;

        from("seda:response")
                .to("xslt:xsl/brsMock.xsl")
                .removeHeaders("*", "debug.*")
                .choice()
                    .when(simple("${header.debug.url}"))
                    .log(LoggingLevel.DEBUG, "callback url: ${header.debug.url}")
                    .setHeader(Exchange.DESTINATION_OVERRIDE_URL).simple("${in.header.debug.url}")
                    .endChoice()
                    .otherwise()
                .end()
                .choice()
                    .when(simple("${header.debug.sleep}"))
                    .log(LoggingLevel.DEBUG, "callback sleep: ${header.debug.sleep}")
                    .delay().simple("${in.header.debug.sleep}")
                    .endChoice()
                    .otherwise()
                .end()
                .log(LoggingLevel.INFO, "brs.SMPCardServices", ">> ${body}")
                .to(ExchangePattern.InOut, "cxf:bean:brs.SMPCardServices")
                .log(LoggingLevel.INFO, "brs.SMPCardServices", "<< ${body}")
        ;
    }
}
