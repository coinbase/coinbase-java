package com.coinbase.api.auth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Hex;
import org.glassfish.jersey.message.MessageBodyWorkers;

@Provider
public class HmacClientFilter implements ClientRequestFilter {

    public static final String API_KEY_NAME = "hmacApiKey";
    public static final String API_SECRET_NAME = "hmacApiSecret";

   @Context
    private MessageBodyWorkers workers;

   @Context
   private Configuration configuration;

    // TODO
    // private static final Logger log =
    // LoggerFactory.getLogger(HmacClientFilter.class);

    public HmacClientFilter() {
	
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void filter(ClientRequestContext requestContext) throws IOException {
	String apiKey = (String) configuration.getProperty(API_KEY_NAME);
	String apiSecret = (String) configuration.getProperty(API_SECRET_NAME);

	String nonce = String.valueOf(System.currentTimeMillis());
	String url = requestContext.getUri().toString();
	String body = null;

	// Serialize the body if necessary
	if (requestContext.hasEntity()) {

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    // get most appropriate MBW
	    final MessageBodyWriter messageBodyWriter = workers.getMessageBodyWriter(
		    requestContext.getEntityClass(),
		    requestContext.getEntityType(),
		    requestContext.getEntityAnnotations(),
		    requestContext.getMediaType()
		    );

	    // use the MBW to serialize myBean into baos
	    messageBodyWriter.writeTo(
		    requestContext.getEntity(),
		    requestContext.getEntityClass(),
		    requestContext.getEntityType(),
		    requestContext.getEntityAnnotations(),
		    requestContext.getMediaType(),
		    new MultivaluedHashMap<String, Object>(),
		    baos);

	    body = baos.toString();
	    
	    requestContext.setEntity(body);
	}

	String message = nonce + url + (body != null ? body : "");
	
// TODO remove
	System.out.println("Canonical message: #" + message + "#");

	Mac mac = null;
	try {
	    mac = Mac.getInstance("HmacSHA256");
	    mac.init(new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256"));
	} catch (Throwable t) {
	    throw new IOException(t);
	}

	String signature = new String(Hex.encodeHex(mac.doFinal(message.getBytes())));
	
// TODO remove
	System.out.println("Signature: #" + signature + "#");

	requestContext.getHeaders().add("ACCESS_KEY", apiKey);
	requestContext.getHeaders().add("ACCESS_SIGNATURE", signature);
	requestContext.getHeaders().add("ACCESS_NONCE", nonce);
    }
}
