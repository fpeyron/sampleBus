package fr.boursorama.bus.ttis;

import fr.boursorama.bus.util.broker.BrokerUtil;
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

        from(BrokerUtil.consumer("ttis.consumer"))
                .to("log:brs.call.SMPCardServices.input?level=INFO&showBody=true")
                //.setHeader("messageType").xpath("local-name(/*[1])", String.class)
                //.to("validator:xsd/CSD002Aller.xsd")
                .setBody().constant("<gen:activer xmlns:gen=\"http://generic.monetiq.evolan.sopra.com/\">\n" +
                "         <messageSMPAllerXML>\n" +
                "            <CSD002Aller>\n" +
                "            \t<serviceMetierPublicAller>\n" +
                "            \t\t<refExtEmetteur>myRef</refExtEmetteur>\n" +
                "            \t\t<idService>232</idService>\n" +
                "            \t\t<version>1.0</version>\n" +
                "            \t\t<userid>012121</userid>\n" +
                "            \t\t<codLang>23232</codLang>\n" +
                "            \t\t<refExtDemSMP>2121</refExtDemSMP>\n" +
                "            \t</serviceMetierPublicAller>\n" +
                "            \t<numPan>1212323453535</numPan>\n" +
                "            \t<datFinValidite>201602</datFinValidite>\n" +
                "            \t<codDemande>A232</codDemande>\n" +
                "            \t<codCanalAcquisition>21DSD22</codCanalAcquisition>\n" +
                "            </CSD002Aller>\n" +
                "         </messageSMPAllerXML>\n" +
                "      </gen:activer>")
                .to("cxf:bean:ttis.SMPCardServices")
                .to("log:brs.call.SMPCardServices.output?level=INFO&showBody=true")
        ;

        from("cxf:bean:brs.SMPCardServices")
                .to("log:brs.SMPCardServices.input?level=INFO&showBody=true")
                .transform().xpath("//messageSMPAllerXML/*")
                .convertBodyTo(String.class)
                .setHeader("messageType").xpath("local-name(/*[1])", String.class)
                .setHeader("messageVersion").xpath("local-name(/*[1]/serviceMetierPublicRetour/version/text())", String.class)
                //.log(LoggingLevel.INFO,"${header.messageType} ${header.messageVersion}")
                .to("validator:xsd/CSD002Retour.xsd")
                        //.to("xslt:xsl/brsMock.xsl")
                        //.convertBodyTo(String.class)
                .to(ExchangePattern.InOnly, BrokerUtil.producer("ttis.producer"))
                .transform().constant("<gen:notifierResponse xmlns:gen=\"http://generic.ttis.bus.boursorama.fr/\"><messageSMPRetourXML /></gen:notifierResponse>")
                .to("log:brs.SMPCardServices.output?level=INFO&showBody=true")
        ;

    }
}
