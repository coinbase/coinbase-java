package com.coinbase.api.entity;

import java.util.List;

public class OrdersResponse extends Response {
    private List<OrderNode> _orders;
    
    public List<OrderNode> getOrders() {
	return _orders;
    }

    public void setOrders(List<OrderNode> orders) {
	_orders = orders;
    }
}
