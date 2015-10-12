package fr.boursorama.bus.mock.conf;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by florent on 02/10/15.
 */


@Configuration
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
public class SoapConfig {

    @Autowired
    private Bus bus;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        CXFServlet servlet = new CXFServlet();
        return new ServletRegistrationBean(servlet, "/soap/*");
    }

    @Bean(name = "ttis.SMPCardServices")
    public CxfEndpoint ttisSMPCardServices() {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setBus(bus);
        endpoint.setEndpointName(new QName("http://generic.monetiq.evolan.sopra.com/", "SMPEvolanPort"));
        endpoint.setServiceName(new QName("http://generic.monetiq.evolan.sopra.com/", "SMPCardServices"));
        endpoint.setServiceClass(this.getClass());
        endpoint.setWsdlURL("wsdl/ttis.SMPCardServices.wsdl");
        endpoint.setAddress("/ttis.SMPCardServices");
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
        endpoint.setWsdlURL("wsdl/brs.SMPCardServices.wsdl");
        endpoint.setAddress("http://localhost:8080/boursorama-bus-ttis/soap/brs.SMPCardServices");
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
