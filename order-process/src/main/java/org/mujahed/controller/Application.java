package org.mujahed.controller;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.order.Order;
import demo.order.OrderProcess;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource({ "classpath:beans.xml", "classpath:client-beans.xml" })
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new CXFServlet(), "/service/*");
	}

	@Autowired
	private ApplicationContext context;

	@RequestMapping("/client")
	String client() {
		OrderProcess client = (OrderProcess) context.getBean("orderClient");

		Order order = new Order();
		order.setCustomerID(java.util.UUID.randomUUID().toString());
		order.setItemID(java.util.UUID.randomUUID().toString());
		order.setQty(2);
		order.setPrice(200.00);
		String orderID = client.processOrder(order);
		String message = (orderID == null) ? "Order not approved"
				: "<h3>Your Order is approved</h3><p><b>Order ID is " + orderID
						+ "</p></b>";
		return message;
	}
}
