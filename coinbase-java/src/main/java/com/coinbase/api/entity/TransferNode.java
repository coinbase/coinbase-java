package com.coinbase.api.entity;

import java.io.Serializable;

public class TransferNode implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 640048507035606528L;
    private Transfer _transfer;

    public Transfer getTransfer() {
        return _transfer;
    }

    public void setTransfer(Transfer transfer) {
        _transfer = transfer;
    }

}
