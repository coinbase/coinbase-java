package com.coinbase.api.entity;

public class TransferResponse extends Response {
    private Transfer _transfer;
    
    public Transfer getTransfer() {
        return _transfer;
    }

    public void setTransfer(Transfer transfer) {
        _transfer = transfer;
    }
}
