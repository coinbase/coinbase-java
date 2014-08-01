package com.coinbase.api.entity;

import java.io.Serializable;

public class OrderNode implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 9143405822047138714L;
    private Order _order;

    public Order getOrder() {
        return _order;
    }

    public void setOrder(Order order) {
        _order = order;
    }
}
