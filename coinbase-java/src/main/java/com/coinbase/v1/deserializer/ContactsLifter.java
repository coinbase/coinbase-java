package com.coinbase.v1.deserializer;

import com.coinbase.v1.entity.Contact;
import com.coinbase.v1.entity.ContactNode;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.List;

public class ContactsLifter extends StdConverter<List<ContactNode>, List<Contact>> {

    public List<Contact> convert(List<ContactNode> nodes) {
	ArrayList<Contact> result = new ArrayList<Contact>();
	
	for (ContactNode node : nodes) {
	    result.add(node.getContact());
	}
	
	return result;
    }

}
