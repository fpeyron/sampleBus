package fr.boursorama.bus.cis;

import fr.boursorama.bus.util.broker.BrokerUtil;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


/**
 * @author fpeyron
 */
@Component
public class CisConsumerRoutebuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(BrokerUtil.consumer("cis.consumer"))
                .to("log:brs.call.SMPCardServices.input?level=INFO&showBody=true")
                .to("log:brs.call.SMPCardServices.output?level=INFO&showBody=true")
        ;

    }
}
