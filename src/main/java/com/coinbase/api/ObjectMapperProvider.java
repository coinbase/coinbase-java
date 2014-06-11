package com.coinbase.api;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    final ObjectMapper defaultObjectMapper;

    public ObjectMapperProvider() {
	defaultObjectMapper = createDefaultMapper();
    }

    public ObjectMapper getContext(Class<?> type) {
	return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
	final ObjectMapper result = new ObjectMapper();
	result.configure(SerializationFeature.INDENT_OUTPUT, true);
	result.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

	return result;
    }
}
