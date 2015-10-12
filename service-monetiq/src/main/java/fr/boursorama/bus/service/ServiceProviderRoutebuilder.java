package fr.boursorama.bus.service;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.infinispan.InfinispanConstants;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by florent on 25/09/15.
 */
@Component
public class ServiceProviderRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration().component("servlet")
                .bindingMode(RestBindingMode.json)
                .setSkipBindingOnErrorCode(true)
        ;

        rest("/1.0/monetiqService")
                .get("version")
                    .description("provider version")
                    .to("direct:version")
                .get("bankCards/{panId}")
                    .description("Get bankCard detail")
                    .outType(BankCard.class)
                    .to("direct:getBankCard")

                .put("bankCards/{panId}")
                    .description("Update bankCard service")
                    .type(BankCard.class)
                    //.outType(WithdrawalTemporary.class)
                    .to("direct:putBankCard")
        ;


        from("direct:version")
                .setBody().simple("${properties:application.version}")
                //.setBody().constant("${properties:version}")
        ;


        from("direct:getBankCard")
                .setHeader("action").constant("getBankCard")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        BankCard bankCard = new BankCard();
                        bankCard.setPanId(exchange.getIn().getHeader("panId", Long.class));
                        bankCard.setActiveSupport(true);
                        bankCard.setActiveSupportNfc(false);
                        bankCard.setPermanentCeilingCashCode("PLAFONDCASH-001");
                        bankCard.setPermanentCeilingPaymentCode("PLAFONDPAYM-001");
                        bankCard.setTemporaryCeilingDate(new Date());
                        bankCard.setTemporaryCeilingCashCode("PLAFONDCASH-002");
                        bankCard.setTemporaryCeilingPaymentCode("PLAFONDPAYM-002");
                        exchange.getIn().setBody(bankCard);
                    }
                })
        ;
        //DataFormat jaxb = new JaxbDataFormat();

        from("direct:putBankCard")
                .onException(InvalidRequestException.class)
                    .handled(true)
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                    .setBody().simple("{ \"code\": \"${header.CamelHttpResponseCode}\" , \"message\": \"${exception.message}\" }")
                .end()
                .setHeader("action").constant("putBankCard")
                // validation
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        if (exchange.getIn().getHeader("panId") == null) {
                            throw new InvalidRequestException("panId in url is mandatory");
                        }
                        if (exchange.getIn().getHeader("contactid") == null) {
                            throw new InvalidRequestException("contactId as header is mandatory");
                        }
                        if (exchange.getIn().getHeader("source") == null) {
                            throw new InvalidRequestException("source as header is mandatory");
                        }

                        exchange.getIn().getBody(BankCard.class).setPanId(exchange.getIn().getHeader("panId", Long.class));
                        exchange.getIn().getBody(BankCard.class).setContactId(exchange.getIn().getHeader("contactid", Long.class));
                    }
                })
                .log(LoggingLevel.INFO, "${body} \n${headers}")

                .choice()
                .when().simple("${body.activeSupportNfc} != null")
                .to("direct:updateSupportNfc")
                .end()
                .setBody().constant(null)
        ;


        from("direct:updateSupportNfc")
                .errorHandler(noErrorHandler())
                .setHeader("action").constant("updateSupportNfc")
                .setHeader(InfinispanConstants.KEY, simple("monetiq-${header.action}-${header.panId}"))
                .setHeader(InfinispanConstants.OPERATION, constant(InfinispanConstants.GET))
                .to("infinispan://localhost?cacheContainer=#cacheManager")
                .choice()
                .when().simple("${header.CamelInfinispanOperationResult} != null")
                .throwException(new InvalidRequestException("updateSupportNfc en cours"))
                .otherwise()
                .setHeader(InfinispanConstants.VALUE).simple(">>${exchangeId}")
                .setHeader(InfinispanConstants.OPERATION, constant(InfinispanConstants.PUT))
                .to("infinispan://localhost?cacheContainer=#cacheManager")
                .end()
                .to(ExchangePattern.InOnly, BrokerUtil.producer("ttis.consumer"))
        ;


    }
}

