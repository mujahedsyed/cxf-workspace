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

```
@WebService
public interface OrderProcess {

	@WebMethod
	String processOrder(Order order);
}
``` 