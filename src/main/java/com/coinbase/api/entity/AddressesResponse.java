package com.coinbase.api.entity;

import java.util.List;

public class AddressesResponse extends Response {
    private List<AddressNode> _addresses;
    
    public List<AddressNode> getAddresses() {
	return _addresses;
    }

    public void setAddresses(List<AddressNode> addresses) {
	_addresses = addresses;
    }
}
