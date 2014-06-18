package com.coinbase.api;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.glassfish.jersey.SslConfigurator;

class CoinbaseSSL {

    public static SSLContext context() throws Exception {
	InputStream in = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/ca-coinbase.jks");
	
	KeyStore trustStore = KeyStore.getInstance("JKS");
	trustStore.load(in, "changeit".toCharArray());
	
	SslConfigurator sslConfig = SslConfigurator.newInstance(true).trustStore(trustStore);

	return sslConfig.createSSLContext();

    }

}
