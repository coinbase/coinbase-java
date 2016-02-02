package com.coinbase.deserializer;

import com.coinbase.entity.Transaction;
import com.coinbase.entity.TransactionNode;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.List;

public class TransactionsLifter extends StdConverter<List<TransactionNode>, List<Transaction>> {

    public List<Transaction> convert(List<TransactionNode> nodes) {
	ArrayList<Transaction> result = new ArrayList<Transaction>();
	
	for (TransactionNode node : nodes) {
	    result.add(node.getTransaction());
	}
	
	return result;
    }

}
