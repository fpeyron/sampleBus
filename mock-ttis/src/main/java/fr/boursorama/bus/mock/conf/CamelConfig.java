package fr.boursorama.bus.mock.conf;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.processor.interceptor.Tracer;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig extends CamelConfiguration {
    @Value("${logging.trace.enabled:false}")
    private Boolean tracingEnabled;

    @Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
        PropertiesComponent pc = new PropertiesComponent();
        pc.setLocation("classpath:application.properties");
        camelContext.addComponent("properties", pc);
        // see if trace logging is turned on
        if (tracingEnabled) {
            camelContext.setTracing(true);
        }

        // set StreamCaching to up
        camelContext.setStreamCaching(true);
        super.setupCamelContext(camelContext);
    }

    @Bean
    public Tracer camelTracer() {
        Tracer tracer = new Tracer();
        tracer.setTraceExceptions(false);
        tracer.setTraceInterceptors(true);
        tracer.setLogName("fr.boursorama.bus.mock.trace");
        return tracer;
    }
}
