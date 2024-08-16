package com.redhat.camel.rest;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class CamelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
//        from("timer:olamundo?period=2s")
//                .to("direct:clientesall");

        restConfiguration().bindingMode(RestBindingMode.auto)
                .component("platform-http")
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/").port(8080)
                .apiContextPath("/openapi")
                .apiProperty("api.title", "Camel Quarkus Demo API")
                .apiProperty("api.version", "1.0.0-SNAPSHOT")
                .apiProperty("cors", "true");

        rest().produces("application/json")
                .get("/restclienteall")
                .route()
                .routeId("restclienteall")
                .to("direct:clientesall")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .endRest();

        from("direct:clientesall").routeId("clientesall")
                .to("sql:select id_pessoa, pesnome, pesemail from cgidb.cgi_pessoas order by pesnome LIMIT 20")
                .marshal().json();
//                .log("${body}");
    }
}
