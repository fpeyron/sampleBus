package fr.boursorama.bus;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
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

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@ComponentScan(basePackages = "fr.boursorama.bus")
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class Application extends SpringBootServletInitializer {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Bus bus;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    public ServletRegistrationBean camelServletRegistration() {

        // Create servlet for Camel Rest Endpoints
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new CamelHttpTransportServlet(), "/rest/*");
        registration.setName("CamelServlet");
        return registration;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        CXFServlet servlet = new CXFServlet();
        return new ServletRegistrationBean(servlet, "/soap/*");
    }

    @Autowired
    Bus cxfBus;

    @Bean(name = "ttis.SMPCardServices")
    public CxfEndpoint ttisSMPCardServices() {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setBus(bus);
        endpoint.setEndpointName(new QName("http://generic.monetiq.evolan.sopra.com/", "SMPEvolanPort"));
        endpoint.setServiceName(new QName("http://generic.monetiq.evolan.sopra.com/", "SMPCardServices"));
        endpoint.setServiceClass(this.getClass());
        endpoint.setWsdlURL("fr/boursorama/bus/ttis/wsdl/ttis.SMPCardServices.wsdl");
        endpoint.setAddress("http://localhost:9080/soap/ttis.SMPCardServices");
        Map<String, Object> properties = new HashMap<String, Object>();
        endpoint.setProperties(properties);
        properties.put("dataformat", "PAYLOAD");
        properties.put("setDefaultBus", true);
        properties.put("schema-validation-enabled", true);
        properties.put("faultStackTraceEnabled", true);

        return endpoint;
    }

    @Bean(name = "brs.SMPCardServices")
    public CxfEndpoint brsSMPCardServices() {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setBus(bus);

        endpoint.setEndpointName(new QName("http://generic.ttis.bus.boursorama.fr/", "SMPEvolanPort"));
        endpoint.setServiceName(new QName("http://generic.ttis.bus.boursorama.fr/", "SMPCardServices"));
        endpoint.setWsdlURL("fr/boursorama/bus/ttis/wsdl/brs.SMPCardServices.wsdl");
        endpoint.setAddress("/brs.SMPCardServices");
        endpoint.setServiceClass(this.getClass());
        Map<String, Object> properties = new HashMap<String, Object>();
        endpoint.setProperties(properties);
        properties.put("dataformat", "PAYLOAD");
        properties.put("setDefaultBus", true);
        properties.put("schema-validation-enabled", true);
        properties.put("faultStackTraceEnabled", true);

        return endpoint;
    }

}

