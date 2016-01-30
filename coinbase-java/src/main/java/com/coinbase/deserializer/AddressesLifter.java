package com.coinbase.deserializer;

import com.coinbase.entity.Address;
import com.coinbase.entity.AddressNode;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.List;

public class AddressesLifter extends StdConverter<List<AddressNode>, List<Address>> {

    public List<Address> convert(List<AddressNode> nodes) {
	ArrayList<Address> result = new ArrayList<Address>();
	
	for (AddressNode node : nodes) {
	    result.add(node.getAddress());
	}
	
	return result;
    }

}
