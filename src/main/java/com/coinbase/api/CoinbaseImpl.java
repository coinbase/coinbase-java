package com.coinbase.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.coinbase.api.entity.Users;
import com.coinbase.api.entity.Users.UserNode.User;

class CoinbaseImpl implements Coinbase {

    private static final String API_BASE_URL = "https://coinbase.com/api/v1/";
    private Client _client = null;
    private WebTarget _base_target = null;

    CoinbaseImpl(CoinbaseBuilder builder) throws Exception {

	ClientConfig clientConfig = new ClientConfig();

	clientConfig.register(ObjectMapperProvider.class);
	clientConfig.register(JacksonFeature.class);

	if (builder.access_token != null) {
	    clientConfig.register(OAuth2ClientSupport.feature(builder.access_token));
	}

	_client = ClientBuilder.newBuilder().withConfig(clientConfig)
		.sslContext(CoinbaseSSL.context()).build();

	_base_target = _client.target(API_BASE_URL);

    }

    public User getUser() {
	WebTarget usersTarget = _base_target.path("users");
	Users response = usersTarget.request(MediaType.APPLICATION_JSON_TYPE).get(Users.class);
	return response.getUsers().get(0).getUser();
    }

}
