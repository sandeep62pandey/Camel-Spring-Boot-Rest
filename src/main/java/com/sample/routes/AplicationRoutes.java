/**
 * 
 */
package com.sample.routes;

import com.sample.model.*;
import com.sample.service.SalesService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class AplicationRoutes extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
	    // configure rest-dsl
        restConfiguration()
           // to use servlet component and run on port 8080
            .component("servlet").port(8080)
            // and enable json/xml binding mode
            .bindingMode(RestBindingMode.json)
            // lets enable pretty printing json responses
            .dataFormatProperty("prettyPrint", "true")
            // lets enable swagger api
            .apiContextPath("api-doc")
            // and setup api properties
            .apiProperty("openapi", "3.0.3 ")
            .apiProperty("api.title", "Rider Auto Parts Order Services")
            .apiProperty("api.description", "Order Service that allows customers to submit orders and query status")
            .apiProperty("api.contact.name", "Rider Auto Parts");


        // error handling to return custom HTTP status codes for the various exceptions

        onException(OrderInvalidException.class)
            .handled(true)
            // use HTTP status 400 when input data is invalid
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
            .setBody(constant(""));

        onException(OrderNotFoundException.class)
            .handled(true)
            // use HTTP status 404 when data was not found
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
            .setBody(constant(""));

		onException(BeanValidationException.class)
				.handled(true)
				// use HTTP status 500 when we had a server side error
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
				.bean(SalesService.class,"frameValidationException(${exception})")
		;

        onException(Exception.class)
            .handled(true)
            // use HTTP status 500 when we had a server side error
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
            .setBody(simple("${exception.message}\n"));
		

		
		
		
		getContext().getGlobalOptions().put("CamelJacksonEnableTypeConverter","true");
		getContext().getGlobalOptions().put("CamelJacksonTypeConverterToPojo","true");
		   rest("/customer").description("Order services")

           .get("/ping").apiDocs(false)
               .to("direct:ping")

           .get("{id}")
           .outType(Order.class)
               .description("Service to get details of an existing order")
               .param().name("id").description("The order id").endParam()
               .responseMessage().code(200).message("The order with the given id").endResponseMessage()
               .responseMessage().code(404).message("Order not found").endResponseMessage()
               .responseMessage().code(500).message("Server error").endResponseMessage()
               .to("direct:dummy")

           .post("/{entityId}/{year}")
           .type(Request.class)

           .outType(User.class)
               .description("Service to submit a new order")
               .responseMessage()
                   .code(200).message("The created order id")
               .endResponseMessage()
               .responseMessage().code(400).message("Invalid input data").endResponseMessage()
               .responseMessage().code(500).message("Server error").endResponseMessage()

               .to("direct:dummy")

           .put()
           .type(Order.class)
               .description("Service to update an existing order")
               .responseMessage().code(400).message("Invalid input data").endResponseMessage()
               .responseMessage().code(500).message("Server error").endResponseMessage()
               .to("bean:orderService?method=updateOrder")

           .delete("{id}")
               .description("Service to cancel an existing order")
               .param().name("id").description("The order id").endParam()
               .responseMessage().code(404).message("Order not found").endResponseMessage()
               .responseMessage().code(500).message("Server error").endResponseMessage()
               .to("bean:orderService?method=cancelOrder(${header.id})");
		
		from("direct:dummy")
				.bean(SalesService.class,"validateJson")
                .log("${body}")
//		.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
		;
	}

}
