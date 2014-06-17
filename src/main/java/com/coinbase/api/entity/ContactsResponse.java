package com.coinbase.api.entity;

import java.util.List;

public class ContactsResponse extends Response {
    private List<ContactNode> _contacts;
    
    public List<ContactNode> getContacts() {
	return _contacts;
    }

    public void setContacts(List<ContactNode> contacts) {
	_contacts = contacts;
    }
}
