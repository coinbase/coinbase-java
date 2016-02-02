package com.coinbase.entity;

public class TransactionResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -8800221662555631113L;
    private com.coinbase.entity.Transaction _transaction;
    
    public com.coinbase.entity.Transaction getTransaction() {
        return _transaction;
    }

    public void setTransaction(com.coinbase.entity.Transaction transaction) {
        _transaction = transaction;
    }
}
