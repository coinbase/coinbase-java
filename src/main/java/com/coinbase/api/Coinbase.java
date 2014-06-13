package com.coinbase.api;

import com.coinbase.api.entity.Response;
import com.coinbase.api.entity.Transaction;
import com.coinbase.api.entity.User;

public interface Coinbase {

    public User getUser();
    
    public Transaction getTransaction(String id);
    
    public Transaction requestMoney(Transaction transaction);

    public void resendRequest(String transactionId);

    public Transaction sendMoney(Transaction transaction);

    public Response getTransactions();
    public Response getTransactions(int page);

}
