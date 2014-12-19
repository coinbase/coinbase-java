package com.coinbase.api;

import com.coinbase.api.entity.Response;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbase.api.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.urlfetch.*;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.appengine.api.urlfetch.FetchOptions.Builder.validateCertificate;


public class CoinbaseConnectionAppengineImpl implements CoinbaseConnection {

    public static Level logLevel = Level.FINE;

    private static final Logger log = Logger.getLogger(CoinbaseConnectionAppengineImpl.class.getName());

    private static final ObjectMapper objectMapper = ObjectMapperProvider.createDefaultMapper();

    private final String _apiKey;
    private final String _apiSecret;
    private String _accessToken;

    public CoinbaseConnectionAppengineImpl(String apiKey, String apiSecret, String accessToken) {
        this._apiKey = apiKey;
        this._apiSecret = apiSecret;
        this._accessToken = accessToken;
    }

    @Override
    public String doHttp(URL url, String method, Object requestBody) throws IOException, CoinbaseException {
        log.log(logLevel, "URL:" + url);
        HTTPRequest request = new HTTPRequest(url, HTTPMethod.valueOf(method), validateCertificate());

        String body = null;
        if (requestBody != null) {
            body = objectMapper.writeValueAsString(requestBody);
            request.setHeader(new HTTPHeader("Content-Type", "application/json"));
        }

        if (_apiKey != null && _apiSecret != null) {
            doHmacAuthentication(url, body, request);
        } else if (_accessToken != null) {
            doAccessTokenAuthentication(request);
        }

        if (body != null) {
            request.setPayload(body.getBytes("UTF-8"));
        }

        HTTPResponse response = URLFetchServiceFactory.getURLFetchService().fetch(request);
        byte[] content = response.getContent();
        String responseData = new String(content, "UTF-8");
        log.log(logLevel, "Response code: " + response.getResponseCode() + "\nResponse: " + responseData);

        switch (response.getResponseCode()) {
            case HttpURLConnection.HTTP_NO_CONTENT:
            case HttpURLConnection.HTTP_OK:
                return responseData;
            default:
                Response coinbaseResponse;
                for (HTTPHeader httpHeader : response.getHeaders()) {
                    if (httpHeader.getName().equalsIgnoreCase("Content-Type") && httpHeader.getValue().toLowerCase().contains("json")) {
                        try {
                            coinbaseResponse = CoinbaseImpl.deserialize(responseData, Response.class);
                        } catch (Exception ex) {
                            throw new CoinbaseException(responseData);
                        }
                        CoinbaseImpl.handleErrors(coinbaseResponse);
                        break;
                    }
                }
                if (response.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    throw new UnauthorizedException(responseData);
                }
                throw new CoinbaseException("Coinbase error");
        }
    }

    @Override
    public String getAccessToken() {
        return this._accessToken;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this._accessToken = accessToken;
    }

    private void doHmacAuthentication(URL url, String body, HTTPRequest request) throws IOException {
        String nonce = String.valueOf(System.currentTimeMillis());

        String message = nonce + url.toString() + (body != null ? body : "");

        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(_apiSecret.getBytes(), "HmacSHA256"));
        } catch (Throwable t) {
            throw new IOException(t);
        }

        String signature = new String(Hex.encodeHex(mac.doFinal(message.getBytes())));

        request.setHeader(new HTTPHeader("ACCESS_KEY", _apiKey));
        request.setHeader(new HTTPHeader("ACCESS_SIGNATURE", signature));
        request.setHeader(new HTTPHeader("ACCESS_NONCE", nonce));
    }

    private void doAccessTokenAuthentication(HTTPRequest request) {
        request.setHeader(new HTTPHeader("Authorization", "Bearer " + _accessToken));
    }
}
