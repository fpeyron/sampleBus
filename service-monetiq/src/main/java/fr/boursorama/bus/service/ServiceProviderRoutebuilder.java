package fr.boursorama.bus.service;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.infinispan.InfinispanConstants;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
                //.dataFormatProperty("prettyPrint", "true")
        ;

        rest("/1.0/monetiqService")
                .get("version")
                    .description("provider version")
                    .to("direct:version")

                .get("{panId}/withdrawalTemporary")
                    .description("Get contract service detail")
                    .outType(WithdrawalTemporary.class)
                    .to("direct:getWithdrawalTemporally")

                .put("{panId}/withdrawalTemporary")
                    .description("Update contract service")
                .type(WithdrawalTemporary.class)
                        .outType(WithdrawalTemporary.class)
                        //.outType(WithdrawalTemporary.class)
                    .to("direct:putWithdrawalTemporally")
        ;


        from("direct:version")
                .setBody().constant("version 1.2.3")
        ;


        from("direct:getWithdrawalTemporally")
                .setHeader("action").constant("getWithdrawalTemporally")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        WithdrawalTemporary withdrawalTemporary = new WithdrawalTemporary();
                        withdrawalTemporary.setPanId(exchange.getIn().getHeader("panId", Long.class));
                        withdrawalTemporary.setPaymentCode("PLAFOND005");
                        withdrawalTemporary.setCashAmount(new Long(3000));
                        withdrawalTemporary.setDueDate(new Date());
                        exchange.getIn().setBody(withdrawalTemporary);

                    }
                })
        ;
        DataFormat jaxb = new JaxbDataFormat();

        from("direct:putWithdrawalTemporally")
                .setHeader("action").constant("putWithdrawalTemporally")
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

                        exchange.getIn().getBody(WithdrawalTemporary.class).setPanId(exchange.getIn().getHeader("panId", Long.class));
                        exchange.getIn().getBody(WithdrawalTemporary.class).setContactId(exchange.getIn().getHeader("contactid", Long.class));
                        exchange.getIn().getBody(WithdrawalTemporary.class).setSource(exchange.getIn().getHeader("source", String.class));
                    }
                })
                        //.marshal(jaxb)
                .log(LoggingLevel.INFO, "${body} \n${headers}")

                .setHeader(InfinispanConstants.KEY, simple("service-monetiq-${header.panId}"))
                .setHeader(InfinispanConstants.VALUE, constant(""))
                .setHeader(InfinispanConstants.OPERATION, constant(InfinispanConstants.PUT))
                .to("infinispan://localhost?cacheContainer=#cacheManager")

                .to(ExchangePattern.InOnly, BrokerUtil.producer("ttis.consumer"))
                .setBody().constant(null)
        ;



    }
}

