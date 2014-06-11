package com.coinbase.api;

import javax.net.ssl.SSLContext;

import org.glassfish.jersey.SslConfigurator;

public class CoinbaseSSL {
	
	public static SSLContext context() {
		SslConfigurator sslConfig = SslConfigurator.newInstance(true)
		        .trustStoreFile("/com/coinbase/api/ca-coinbase.jks")
		        .trustStorePassword("changeit");
		
		return sslConfig.createSSLContext();
		
	}

}
