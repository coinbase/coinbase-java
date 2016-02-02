package com.coinbase.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.List;
import java.util.ListIterator;

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
