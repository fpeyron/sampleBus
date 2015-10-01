package fr.boursorama.bus.mock;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ComponentScan(basePackages = "fr.boursorama.bus.mock")
@ImportResource({"META-INF/spring/cxf-context.xml"})
//, "META-INF/spring/camel-context.xml" })
public class Application extends SpringBootServletInitializer {

    private static final String CAMEL_URL_MAPPING = "/camel/*";
    private static final String CAMEL_SERVLET_NAME = "CamelServlet";
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    /*
      @Bean
      public ServletRegistrationBean camelServletRegistration() {

          // Create servlet for Camel Rest Endpoints
          ServletRegistrationBean registration = new ServletRegistrationBean(
                  new CamelHttpTransportServlet(), CAMEL_URL_MAPPING);
          registration.setName(CAMEL_SERVLET_NAME);
          return registration;
      }
  */

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        CXFServlet servlet = new CXFServlet();
        return new ServletRegistrationBean(servlet, "/soap/*");
    }

}

