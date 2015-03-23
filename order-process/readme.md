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
Execute below command and navigate to http://localhost:8080/

```
mvn clean package spring-boot:run
```

If there are no errors the website displays the hosted WSDL address:

```
Endpoint address: http://localhost:8080/OrderProcess
WSDL : {http://order.demo/}OrderProcessImplService
Target namespace: http://order.demo/
```

If there are no customization on webservice annotation than the hosted WSDL address is /<context-root>/<SIB>Service. If a serviceName attribute is specified on @WebService annotation of the SEI than the hosted WSDL will be /<context-root>/serviceName.

### Developing a client

 