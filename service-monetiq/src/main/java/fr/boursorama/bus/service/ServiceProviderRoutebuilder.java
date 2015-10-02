package fr.boursorama.bus.service;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;

import java.util.Calendar;
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
                .dataFormatProperty("prettyPrint", "true")
        ;

        rest("/bankingCard")
                .get("version")
                    .description("provider version")
                    .to("direct:version")
                .get("{panId}/withdrawalTemporally")
                    .description("Get contract service detail")
                    .outType(WithdrawalTemporally.class)
                    .to("direct:getWithdrawalTemporally")
                .put("{panId}/withdrawalTemporally")
                    .description("Update contract service")
                    .type(WithdrawalTemporally.class)
                    //.outType(WithdrawalTemporally.class)
                    .to("direct:putWithdrawalTemporally")
        ;


        from("direct:version")
                .setBody().constant("version 1.2.3")
        ;


        from("direct:getWithdrawalTemporally")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        WithdrawalTemporally withdrawalTemporally = new WithdrawalTemporally();
                        withdrawalTemporally.setPanId(exchange.getIn().getHeader("panId", Long.class));
                        withdrawalTemporally.setPaymentCode("PLAFOND005");
                        withdrawalTemporally.setCashAmount(new Long(3000));
                        withdrawalTemporally.setDueDate(new Date());
                        exchange.getIn().setBody(withdrawalTemporally);

                    }
                })
        ;
        DataFormat jaxb = new JaxbDataFormat();

        from("direct:putWithdrawalTemporally")
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

                        exchange.getIn().getBody(WithdrawalTemporally.class).setPanId(exchange.getIn().getHeader("panId", Long.class));
                        exchange.getIn().getBody(WithdrawalTemporally.class).setContactId(exchange.getIn().getHeader("contactid", Long.class));
                        exchange.getIn().getBody(WithdrawalTemporally.class).setSource(exchange.getIn().getHeader("source", String.class));
                    }
                })
                        //.marshal(jaxb)
                .log(LoggingLevel.INFO, "${body} \n${headers}")
                .to(ExchangePattern.InOnly, BrokerUtil.producer("ttis.consumer"))
                .setBody().constant(null)
        ;




        from("direct:callTtis")
                .removeHeaders("*", "debug.*")
                .log(LoggingLevel.INFO, ">>> ${body}")
                .to(ExchangePattern.InOut, BrokerUtil.producer("ttis.consumer"))
                .log(LoggingLevel.INFO, "<<< ${body}")
        ;

    }
}

