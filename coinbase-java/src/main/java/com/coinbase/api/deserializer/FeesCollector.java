package com.coinbase.api.deserializer;

import java.util.HashMap;
import java.util.List;

import org.joda.money.Money;

import com.fasterxml.jackson.databind.util.StdConverter;

public class FeesCollector extends StdConverter<List<HashMap<String, Money>>, HashMap<String, Money>> {

    public HashMap<String, Money> convert(List<HashMap<String, Money>> value) {
	HashMap<String, Money> result = new HashMap<String, Money>();
	for (HashMap<String, Money> map : value) {
	    result.putAll(map);
	}
	return result;
    }

}
