package com.coinbase.api.entity;

import java.io.Serializable;

public class ContactNode implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 642700372938561693L;
    private Contact _contact;

    public Contact getContact() {
        return _contact;
    }

    public void setContact(Contact contact) {
        _contact = contact;
    }
}
