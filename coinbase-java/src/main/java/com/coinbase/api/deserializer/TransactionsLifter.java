package com.coinbase.api.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.TransactionNode;
import com.fasterxml.jackson.databind.util.StdConverter;

public class TransactionsLifter extends StdConverter<List<TransactionNode>, List<Transaction>> {

    public List<Transaction> convert(List<TransactionNode> nodes) {
	ArrayList<Transaction> result = new ArrayList<Transaction>();
	
	for (TransactionNode node : nodes) {
	    result.add(node.getTransaction());
	}
	
	return result;
    }

}
