package com.coinbase.v1.deserializer;

import com.coinbase.v1.entity.Order;
import com.coinbase.v1.entity.OrderNode;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.ArrayList;
import java.util.List;

public class OrdersLifter extends StdConverter<List<OrderNode>, List<Order>> {

    public List<Order> convert(List<OrderNode> nodes) {
	ArrayList<Order> result = new ArrayList<Order>();
	
	for (OrderNode node : nodes) {
	    result.add(node.getOrder());
	}
	
	return result;
    }

}
