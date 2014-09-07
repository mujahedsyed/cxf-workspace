package com.mujahed.cxf;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.order.Order;
import demo.order.OrderProcess;

public final class Client {

	public Client() {
	}

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "client-beans.xml" });

		OrderProcess client = (OrderProcess) ctx.getBean("orderClient");

		Order order = new Order();
		order.setCustomerID("C001");
		order.setItemID("I001");
		order.setPrice(200.00);
		order.setQty(100);

		String orderID = client.processOrder(order);
		String message = (orderID == null) ? "Order not approved"
				: "Order approved, order ID is " + orderID;
		System.out.println(message);
		ctx.close();
		System.exit(0);
		
	}
}
