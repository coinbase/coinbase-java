package com.coinbase.api.deserializer;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import com.coinbase.api.ObjectMapperProvider;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DateTimeDeserializerTest {
    
    private ObjectMapper mapper;
    
    public static class TestClass {
	
	DateTime _createdAt;

	public DateTime getCreatedAt() {
	    return _createdAt;
	}
	
	@JsonDeserialize(using=DateTimeDeserializer.class)
	public void setCreatedAt(DateTime createdAt) {
	    _createdAt = createdAt;
	}
	
    }

    @Before
    public void setUp() {
	mapper = ObjectMapperProvider.createDefaultMapper();
    }

    @Test
    public void datetime() throws Exception {
	String json = "{\"created_at\": \"2012-08-02T01:07:48-07:00\"}";
	TestClass tc = mapper.readValue(json, TestClass.class);
	DateTime time = tc.getCreatedAt();
	
	assertEquals(2012, time.getYear());
	assertEquals(8, time.getMonthOfYear());
	assertEquals(2, time.getDayOfMonth());
	assertEquals(1, time.getHourOfDay());
	assertEquals(7, time.getMinuteOfHour());
	assertEquals(48, time.getSecondOfMinute());
    }
    
    @Test(expected = JsonProcessingException.class)
    public void invalidDate() throws Exception {
	String json = "{\"created_at\": \"asdf\"}";
	TestClass tc = mapper.readValue(json, TestClass.class);
	DateTime time = tc.getCreatedAt();
    }

}
