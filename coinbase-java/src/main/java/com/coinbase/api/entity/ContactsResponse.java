package com.coinbase.api.entity;

import java.util.List;

import com.coinbase.api.deserializer.ContactsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ContactsResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = 1579609741624298006L;
    private List<Contact> _contacts;
    
    public List<Contact> getContacts() {
	return _contacts;
    }

    @JsonDeserialize(converter=ContactsLifter.class)
    public void setContacts(List<Contact> contacts) {
	_contacts = contacts;
    }
}
