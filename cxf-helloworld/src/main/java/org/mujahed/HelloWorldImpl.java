package org.mujahed;

import javax.jws.WebService;

@WebService(endpointInterface = "org.mujahed.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

	@Override
	public String sayHi(String text) {
		return "Salam " + text;
	}

}
