package com.coinbase.api.deserializer;

import java.util.List;
import java.util.ListIterator;

import com.fasterxml.jackson.databind.util.StdConverter;

public class ErrorsCollector extends StdConverter<List<String>, String> {

    public String convert(List<String> errors) {
	StringBuilder sb = new StringBuilder();
	ListIterator<String> it = errors.listIterator();
	
	while (it.hasNext()) {
	    sb.append(it.next());
	    if (it.hasNext()) {
		sb.append(", ");
	    }
	}
	
	return sb.toString();
    }

}
