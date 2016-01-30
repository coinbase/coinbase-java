package com.coinbase.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.coinbase.entity.Contact;
import com.coinbase.entity.ContactNode;
import com.fasterxml.jackson.databind.util.StdConverter;

public class ContactsLifter extends StdConverter<List<ContactNode>, List<Contact>> {

    public List<Contact> convert(List<ContactNode> nodes) {
	ArrayList<Contact> result = new ArrayList<Contact>();
	
	for (ContactNode node : nodes) {
	    result.add(node.getContact());
	}
	
	return result;
    }

}
