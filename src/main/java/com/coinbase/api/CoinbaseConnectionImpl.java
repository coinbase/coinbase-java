package com.coinbase.api;

import com.coinbase.api.entity.Response;
import com.coinbase.api.exception.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class CoinbaseConnectionImpl implements CoinbaseConnection {

    private final ObjectMapper objectMapper;

    private final SSLSocketFactory _socketFactory;
    private final String _apiKey;
    private final String _apiSecret;
    private final String _accessToken;

    public CoinbaseConnectionImpl(SSLContext sslContext, ObjectMapper objectMapper, String apiKey, String apiSecret, String accessToken) {
        if (sslContext != null) {
            _socketFactory = sslContext.getSocketFactory();
        } else {
            _socketFactory = CoinbaseSSL.getSSLContext().getSocketFactory();
        }
        this.objectMapper = objectMapper;
        this._apiKey = apiKey;
        this._apiSecret = apiSecret;
        this._accessToken = accessToken;
    }

    @Override
    public String doHttp(URL url, String method, Object requestBody) throws IOException, CoinbaseException {
        URLConnection urlConnection = url.openConnection();
        if (!(urlConnection instanceof HttpsURLConnection)) {
            throw new RuntimeException(
                    "Custom Base URL must return javax.net.ssl.HttpsURLConnection on openConnection.");
        }
        HttpsURLConnection conn = (HttpsURLConnection) urlConnection;
        conn.setSSLSocketFactory(_socketFactory);
        conn.setRequestMethod(method);

        String body = null;
        if (requestBody != null) {
            body = objectMapper.writeValueAsString(requestBody);
            conn.setRequestProperty("Content-Type", "application/json");
        }

        if (_apiKey != null && _apiSecret != null) {
            doHmacAuthentication(url, body, conn);
        } else if (_accessToken != null) {
            doAccessTokenAuthentication(conn);
        }

        if (body != null) {
            conn.setDoOutput(true);
            OutputStream outputStream = conn.getOutputStream();
            try {
                outputStream.write(body.getBytes(Charset.forName("UTF-8")));
            } finally {
                outputStream.close();
            }
        }

        InputStream is = null;
        InputStream es = null;
        try {
            is = conn.getInputStream();
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            es = conn.getErrorStream();
            String errorBody = null;
            if (es != null) {
                errorBody = IOUtils.toString(es, "UTF-8");
                if (errorBody != null && conn.getContentType().toLowerCase().contains("json")) {
                    Response coinbaseResponse;
                    try {
                        coinbaseResponse = CoinbaseImpl.deserialize(errorBody, Response.class);
                    } catch (Exception ex) {
                        throw new CoinbaseException(errorBody);
                    }
                    CoinbaseImpl.handleErrors(coinbaseResponse);
                }
            }
            if (HttpsURLConnection.HTTP_UNAUTHORIZED == conn.getResponseCode()) {
                throw new UnauthorizedException(errorBody);
            }
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }

            if (es != null) {
                es.close();
            }
        }
    }

    private void doHmacAuthentication(URL url, String body, HttpsURLConnection conn) throws IOException {
        String nonce = String.valueOf(System.currentTimeMillis());

        String message = nonce + url.toString() + (body != null ? body : "");

        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(_apiSecret.getBytes(), "HmacSHA256"));
        } catch (Throwable t) {
            throw new IOException(t);
        }

        String signature = new String(Hex.encodeHex(mac.doFinal(message.getBytes())));

        conn.setRequestProperty("ACCESS_KEY", _apiKey);
        conn.setRequestProperty("ACCESS_SIGNATURE", signature);
        conn.setRequestProperty("ACCESS_NONCE", nonce);
    }

    private void doAccessTokenAuthentication(HttpsURLConnection conn) {
        conn.setRequestProperty("Authorization", "Bearer " + _accessToken);
    }
}

