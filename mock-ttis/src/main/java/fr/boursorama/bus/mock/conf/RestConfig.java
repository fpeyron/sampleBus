package fr.boursorama.bus.mock.conf;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by florent on 02/10/15.
 */


@Configuration
public class RestConfig {

    @Bean
    public ServletRegistrationBean camelServletRegistration() {

        // Create servlet for Camel Rest Endpoints
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new CamelHttpTransportServlet(), "/rest/*");
        registration.setName("CamelServlet");
        return registration;
    }

}
