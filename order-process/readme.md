# Apache CXF with Spring Boot

## What is a Web Service?
A Web service is a software system identified by a URI whose public interfaces and bindings are defined and described using XML (specifically WSDL). Its definition can be discovered by other software systems. These systems may then interact with the web service in a manner prescribed by its definition, using XML-based messages conveyed by Internet protocols.

Two of the most widely used approaches for developing web services are SOAP and REST.

## CXF Introduction
Apache CXF is an open source web service framework that provides an easy to use, standard-based programming mdoel for developing web services.

### Why CXF?
  - Support for web service standards like JAX-WS, SOAP, WSDL, MTOM, WS-* Standards.
  - Support for POJO.
  - Frontend programming API's: CXF frontends programming APIs can be used to develop web services and web service clients. CXF supports two types of frontends, namely standard-based JAX-WS, and simple frontend.
  - Tools Support (java to wsdl, wsdl to java and many more).
  - Support for RESTful services.
  - Support for different transports and bindings; CXF supports JAXB and AEGIS data binding apart from SOAP and HTTP protocol binding. CXF supports differnet kinds of transport protocols such as HTTP, HTTP(s), JMS and CXF Local protocol that allow service-to-service communication within the single JVM.
   - Support for non-XML binding.
   - Ease of use.
   - Flexible deployment
  
## Developing a service
JAX-WS frontend offers two ways of developing a web service—Code-first and Contract-first. In web service terminology, Code-first is termed as the Bottoms Up approach, and Contract-first is referred to as the Top Down approach. To develop a service, we typically perform the following steps:
  - Create a Service Endpoint Interface (SEI) and define a business method to be used with the web service.
  - Create the implementation class and annotate it as a web service.
  - Create beans.xml and define the service class as a Spring bean using a JAX-WS frontend.

### Creating a Service
The following code illustrates the OrderProcess SEI

```java
@WebService
public interface OrderProcess {

	@WebMethod
	String processOrder(Order order);
}
```

The @WebService annotation is placed right above the interface definition. It signifies that this interface is not an ordinary interface but a web service interface. This interface is known as Service Endpoint Interface and will have a business method exposed as a service method to be invoked by the client. 

The @WebService annotation is part of the JAX-WS annotation library. JAX-WS provides a library of annotations to turn Plain Old Java classes into web services and specifies detailed mapping from a service defined in WSDL to the Java classes that will implement that service.

The javax.jws.@WebMethod annotation is optional and is used for customizing the web service operation. The @WebMethod annotation provides the operation name and the action elements which are used to customize the name attribute of the operation and the SOAP action element in the WSDL document.

The @XmlRootElement is part of the Java Architecture for XML Binding (JAXB) annotation library. JAXB provides data binding capabilities by providing a convenient way to map XML schema to a representation in Java code. The JAXB shields the conversion of XML schema messages in SOAP messages to Java code without having the developers know about XML and SOAP parsing. CXF uses JAXB as the default data binding component.

The attributes contained within the Order object by default are mapped to @XmlElement . The @XmlElement annotations are used to define elements within the XML. The @XmlRootElement and @XmlElement annotations allow you to customize the namespace and name of the XML element. If no customizations are provided, then the JAXB runtime by default would use the same name of attribute for the XML element. CXF handles this mapping of Java objects to XML.

### Developing a Service Implementation Class
Implementation class (OrderProcessImpl) also has @WebService annotation. 

### Spring-based server bean
CXF uses spring-based configuration files to publish web service endpoints.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:jaxws="http://cxf.apache.org/jaxws" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
	http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd
	">

	<import resource="classpath:META-INF/cxf/cxf.xml" />
	<import resource="classpath:META-INF/cxf/cxf-extension-soap.xml" />
	<import resource="classpath:META-INF/cxf/cxf-servlet.xml" />

	<jaxws:endpoint id="orderProcess" implementor="demo.order.OrderProcessImpl" address="/OrderProcess" />

</beans>

```
The above spring configuration file defines a series of <import> statements. It imports cxf.xml , cxf-extension-soap.xml , and cxf-servlet.xml . These files are Spring-based configuration files that define core components of CXF. They are used to kick start CXF runtime and load the necessary infrastructure objects such as WSDL manager, conduit manager, destination factory manager, and so on.

The <jaxws:endpoint> element in the beans.xml file specifies the OrderProcess web service as a JAX-WS endpoint. The element is defined with the following three attributes:
  - **id** —specifies a unique identifier for a bean. In this case, jaxws:endpoint is a bean, and the id name is orderProcess.
  - **implementor** — specifies the actual web service implementation class. In this case, our implementor class is OrderProcessImpl.
  - **address** — specifies the URL address where the endpoint is to be published.
  
The URL address must to be relative to the web context. For our example, the endpoint will be published using the relative path /OrderProcess. 

The <jaxws:endpoint> element signifies that the CXF internally uses JAX-WS frontend to publish the web service. This element definition provides a short and convenient way to publish a web service. A developer need not have to write any Java class to publish a web service.

### Running the service
Execute below command and navigate to http://localhost:8080/service

```
mvn clean package spring-boot:run
```

If there are no errors the website displays the hosted WSDL address:

```
Endpoint address: http://localhost:8080/service/OrderProcess
WSDL : {http://order.demo/}OrderProcessImplService
Target namespace: http://order.demo/
```

**If there are no customization on webservice annotation than the hosted WSDL address is /<context-root>/<SIB>Service. If a serviceName attribute is specified on @WebService annotation of the SEI than the hosted WSDL will be /<context-root>/serviceName.**

### Developing a client
To develop a client we start by writing spring based client configuration file which has jaxws-client bean in it. The following code illustrates client-beans.xml:

```xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:jaxws="http://cxf.apache.org/jaxws" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
	http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd">

	<jaxws:client id="orderClient" serviceClass="demo.order.OrderProcess" 
	         address="http://localhost:8080/service/OrderProcess"></jaxws:client>

</beans>

```

 The <jaxws:client> element in the client-beans.xml file specifies the client bean using JAX-WS frontend. The element is defined with the following three attributes:
 - **id** — specifies a unique identifier for a bean. In this case, jaxws:client is a bean and the id name is orderClient . The bean will represent an SEI.
 - **serviceClass** — specifies the web service SEI. In this case our SEI class is OrderProcess
 - **address** — specifies the URL address where the endpoint is published. 

<jaxws:client> signifies the client bean that represents an OrderProcess SEI. The client application will make use of this SEI to invoke the web service.

#### Developing the client code
Below is an example of writing spring boot client that will invoke the deployed service.

```java
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

```

CXFServlet acts as a front runner component that initiates the CXF environment. 
**Note** The Spring Boot embedded servlet features are designed to work with Servlet and ServletRegistration @Beans, and not with the ContextLoaderListener. That is why we have defined a ServletRegistration for cxf servlet.

## CXF Architecture
The architecture of CXF is built upon the following components:

 - Bus
 - Frontend
 - Messaging and Interceptors
 - Service Model
 - Data bindings
 - Protocol bindings
 - Transport

![Alt text](images/cxf-architecture.png?raw=true "CXF Architecture")

### Bus
Bus is the backbone of the CXF architecture. CXF internally uses Spring for its configuration. The CXF bus is comprised of a Spring-based configuration file, namely, cxf.xml which is loaded upon servlet initialization through SpringBusFactory . It wires all the runtime infrastructure components and provides a common application context. The SpringBusFactory scans and loads the relevant configuration files in the META-INF/cxf directory placed in the classpath and accordingly builds the application context. It builds the application context from the following files:
 - META-INF/cxf/cxf.xml
 - META-INF/cxf/cxf-extension.xml
 - META-INF/cxf/cxf-property-editors.xml

In cxf.xml the following fragment shows the bus definition in the cxf.xml file:

```xml
<bean id="cxf" class="org.apache.cxf.bus.spring.SpringBus" destroy-method="shutdown"/>
```

```
org.apache.cxf.bus.spring
Class SpringBus

java.lang.Object
  extended by org.apache.cxf.interceptor.AbstractBasicInterceptorProvider
      extended by org.apache.cxf.bus.**CXFBusImpl**
          extended by org.apache.cxf.bus.extension.ExtensionManagerBus
              extended by org.apache.cxf.bus.spring.SpringBus
```

The core bus component is **CXFBusImpl** . The class acts more as an interceptor provider for incoming and outgoing requests to a web service endpoint. These interceptors, once defined, are available to all the endpoints in that context. The cxf.xml file also defines other infrastructure components such as BindingFactoryManager, ConduitFactoryManager, and so on. These components are made available as bus extensions. One can access these infrastructure objects using the *getExtension* method.

CXF bus architecture can be overridden, but one must apply caution when overriding the default bus behavior. Since the bus is the core component that loads the CXF runtime, many shared objects are also loaded as part of this runtime. You want to make sure that these objects are loaded when overriding the existing bus implementation. You can extend the default bus to include your own custom components or service objects such as factory managers. You can also add interceptors to the bus bean.

These interceptors defined at the bus level are available to all the endpoints. The following code shows how to create a custom bus:

```java
SpringBeanFactory.createBus("mycxf.xml")
```

SpringBeanFactory class is used to create a bus. You can complement or overwrite the bean definitions that the original cxf.xml file would use. For the CXF to load the mycxf.xml file, it has to be in the classpath or you can use a factory method to load the file. The following code illustrates the use of interceptors at the bus level:

```xml
<bean id="cxf" class="org.apache.cxf.bus.spring.SpringBusImpl">
	<property name="outInterceptors">
		<list>
			<ref bean="myLoggingInterceptor"/>
		</list>
	</property>
</bean>

<bean id="myLogHandler" class="org.mycompany.com.cxf.logging.LoggingInterceptor">
	...
</bean>
```

### Frontend
CXF provides the concept of frontend modeling, which lets you create web services using different frontend APIs. The APIs let you create a web service using simple factory beans and JAX-WS implementation. It also lets you create dynamic web service clients. The primary frontend supported by CXF is JAX-WS.

### JAX-WS
**JAX-WS is a specification that establishes the semantics to develop, publish, and consume web services.** JAX-WS simplifies web service development. It defines Java-based APIs that ease the development and deployment of web services. The specification supports **WS-Basic Profile 1.1** that addresses web service interoperability. It effectively means a web service can be invoked or consumed by
a client written in any language. JAX-WS also defines standards such as JAXB and SAAJ. CXF provides support for complete JAX-WS stack.

**JAXB** provides data binding capabilities by providing a convenient way to map XML schema to a representation in Java code. The JAXB shields the conversion of XML schema messages in SOAP messages to Java code without the developers seeing XML and SOAP parsing.

**SAAJ** provides a standard way of dealing with XML attachments contained in a SOAP message.

JAX-WS also speeds up web service development by providing a library of annotations to turn Plain Old Java classes into web services.

#### What is the correct WSDL generated from this Web Service declaration?

```java
@WebService(name = "LogService", portName = "LogPort", serviceName = "LogWebService")
public class LogServiceImpl {
	public void log(String msg) {
		System.out.println(msg);
	}
}
```

The generated wsdl is:

```xml
<definitions>
	. . .
	<portType name="LogService"> . . .</portType>
	<binding name="LogServiceImplPortBinding" type="..."> . . . </binding>
	
	<service name="LogWebService">
		<port name="LogPort" binding="tns:LogServiceImplPortBinding"> ..</port>
	</service>

</definitions>

```

**Explanation:**
name="LogService" -> wsdl:portType
portName="LogPort" -> wsd:port
serviceName="LogWebService" -> wsdl:service

*serviceName, portName and endpointInterface are not allowed on endpoint interface. If you mark serviceName on interface glassfish ignores it and will not display error.*

### Simple Frontend
Apart from JAX-WS frontend, CXF also supports what is known as 'simple frontend'. The simple frontend provides simple components or Java classes that use reflection to build and publish web services. It is simple because we do not use any annotation to create web services. The simple frontend uses factory components to create a service and the client. 

The following code shows a web service created using simple frontend:

```java
	// Build and publish the service
	OrderProcessImpl orderProcessImpl = new OrderProcessImpl();
	ServerFactoryBean svrFactory = new ServerFactoryBean();
	svrFactory.setServiceClass(OrderProcess.class);
	svrFactory.setAddress("http://localhost:8080/OrderProcess");
	svrFactory.setServiceBean(orderProcessImpl);
	svrFactory.create();
```

### Messaging and Interceptors
One of the important elements of CXF architecture is the Interceptor components. Interceptors are components that intercept the messages exchanged or passed between web service clients and server components. In CXF, this is implemented through the concept of Interceptor chains. The concept of Interceptor chaining is the core functionality of CXF runtime.

The interceptors act on the messages which are sent and received from the web service and are processed in chains. Each interceptor in a chain is configurable, and the user has the ability to control its execution.

![Alt text](images/interceptors.png?raw=true "CXF Interceptors")

The core of the framework is the Interceptor interface. It defines two abstract methods— handleMessage and handleFault . Each of the methods takes the object of type Message as a parameter. A developer implements the handleMessage to process or act upon the message. The handleFault method is implemented to handle the error condition. Interceptors are usually processed in chains with every interceptor in the chain performing some processing on the message in sequence, and the chain moves forward. Whenever an error condition arises, a handleFault method is invoked on each interceptor, and the chain unwinds or moves backwards. 

Interceptors are often organized or grouped into phases. Interceptors providing common functionality can be grouped into one phase. Each phase performs specific message processing. Each phase is then added to the interceptor chain. The chain, therefore, is a list of ordered interceptor phases. The chain can be created for both inbound and outbound messages. A typical web service endpoint will have three interceptor chains:

 - Inbound messages chain
 - Outbound messages chain
 - Error messages chain

There are built-in interceptors such as logging, security, and so on, and the developers can also choose to create custom interceptors.

### Service Model
The Service model, in a true sense, models your service. It is a framework of components that represents a service in a WSDL-like model. It provides functionality to create various WSDL elements such as operations, bindings, endpoints, schema, and so on. The following figure shows the various components that form the Service model:

![Alt text](images/service-model.png?raw=true "Service Model")

CXF frontends internally use the service model to create web services.

### Data binding
CXF supports
two types of data binding components—JAXB and Aegis. CXF uses JAXB as the default data binding component. You can configure your web service to use Aegis data binding as follows:

```xml
<jaxws:endpoint id="orderProcess" implementor="demo.order.OrderProcessImpl" address="/OrderProcess" >
	<jaxws:dataBinding>
		<bean class="org.apache.cxf.aegis.databinding.AegisDatabinding" />
	</jaxws:dataBinding>
</jaxws:endpoint>
```

### Protocol Binding
CXF Support the following binding protocols:
 - SOAP 1.1
 - SOAP 1.2
 - CORBA
 - Pure XML

### Transports
CXF Supports the following transports for its endpoints:
 - HTTP
 - CORBA
 - JMS
 - Local

# Working with CXF Frontends
