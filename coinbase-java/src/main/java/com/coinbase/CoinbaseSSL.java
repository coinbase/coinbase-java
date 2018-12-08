/*
 * Copyright 2018 Coinbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coinbase;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public final class CoinbaseSSL {

    private CoinbaseSSL() {
    }

    private static SSLContext sslContext = null;

    public static synchronized SSLContext getSSLContext() {

        if (sslContext != null) {
            return sslContext;
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
            sslContext = ctx;
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

        return sslContext;
    }
}
