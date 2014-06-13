package com.coinbase.api.entity;

import java.util.List;

import com.coinbase.api.entity.UserNode;

public class Response {

    private String _success;
    private Transaction _transaction;
    private List<UserNode> _users;

    public List<UserNode> getUsers() {
        return _users;
    }
    public void setUsers(List<UserNode> users) {
        _users = users;
    }
    public String getSuccess() {
        return _success;
    }
    public void setSuccess(String success) {
        this._success = success;
    }
    public Transaction getTransaction() {
        return _transaction;
    }
    public void setTransaction(Transaction transaction) {
        _transaction = transaction;
    }
    
}
