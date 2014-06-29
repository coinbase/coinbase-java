package com.coinbase.api;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.glassfish.jersey.SslConfigurator;

class CoinbaseSSL {

    public static SSLContext context() throws Exception {
	
	KeyStore trustStore;
	InputStream trustStoreInputStream;
	
	if (System.getProperty("java.vm.name").equalsIgnoreCase("Dalvik")) {
	    trustStoreInputStream = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/ca-coinbase.bks");
            trustStore = KeyStore.getInstance("BKS");
	} else {
	    trustStoreInputStream = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/ca-coinbase.jks");
            trustStore = KeyStore.getInstance("JKS");
        }

        trustStore.load(trustStoreInputStream, "changeit".toCharArray());
	SslConfigurator sslConfig = SslConfigurator.newInstance(true).trustStore(trustStore);

	return sslConfig.createSSLContext();

    }

}
