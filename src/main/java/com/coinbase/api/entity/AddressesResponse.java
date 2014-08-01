package com.coinbase.api.entity;

import java.util.List;

import com.coinbase.api.deserializer.AddressesLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddressesResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = 3300893137342086524L;
    private List<Address> _addresses;
    
    public List<Address> getAddresses() {
	return _addresses;
    }

    @JsonDeserialize(converter=AddressesLifter.class)
    public void setAddresses(List<Address> addresses) {
	_addresses = addresses;
    }
}
