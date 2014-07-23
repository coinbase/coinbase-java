package com.coinbase.api;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

class CoinbaseSSL {

    private static SSLSocketFactory socketFactory = null;

    public static synchronized SSLSocketFactory getSSLSocketFactory() {

        if (socketFactory != null) {
            return socketFactory;
        }

        KeyStore trustStore = null;
        InputStream trustStoreInputStream = null;
        
        try {
            if (System.getProperty("java.vm.name").equalsIgnoreCase("Dalvik")) {
                trustStoreInputStream = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/ca-coinbase.bks");
                trustStore = KeyStore.getInstance("BKS");
            } else {
                trustStoreInputStream = CoinbaseSSL.class.getResourceAsStream("/com/coinbase/api/ca-coinbase.jks");
                trustStore = KeyStore.getInstance("JKS");
            }
             
            trustStore.load(trustStoreInputStream, "changeit".toCharArray());
    
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tmf.getTrustManagers(), null);
            socketFactory = ctx.getSocketFactory();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (trustStoreInputStream != null) {
                try {
                    trustStoreInputStream.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        return socketFactory;
    }
}
