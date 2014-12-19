package com.coinbase.api.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.coinbase.api.entity.Transfer;
import com.coinbase.api.entity.TransferNode;
import com.fasterxml.jackson.databind.util.StdConverter;

public class TransfersLifter extends StdConverter<List<TransferNode>, List<Transfer>> {

    public List<Transfer> convert(List<TransferNode> nodes) {
	ArrayList<Transfer> result = new ArrayList<Transfer>();
	
	for (TransferNode node : nodes) {
	    result.add(node.getTransfer());
	}
	
	return result;
    }

}
