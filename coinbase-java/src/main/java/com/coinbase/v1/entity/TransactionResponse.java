package com.coinbase.v1.entity;

public class TransactionResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -8800221662555631113L;
    private Transaction _transaction;
    
    public Transaction getTransaction() {
        return _transaction;
    }

    public void setTransaction(Transaction transaction) {
        _transaction = transaction;
    }
}
