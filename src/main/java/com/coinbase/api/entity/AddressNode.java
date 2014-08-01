package com.coinbase.api.entity;

import java.io.Serializable;

public class AddressNode implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3331153178753180883L;
    private Address _address;

    public Address getAddress() {
        return _address;
    }

    public void setAddress(Address address) {
        _address = address;
    }

}
