package com.coinbase.api.entity;

public class OrderResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = 1217894460265345504L;
    private Order _order;
    
    public Order getOrder() {
        return _order;
    }

    public void setOrder(Order order) {
        _order = order;
    }
}
