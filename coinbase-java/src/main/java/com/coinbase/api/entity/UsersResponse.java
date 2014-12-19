package com.coinbase.api.entity;

import java.util.List;

public class UsersResponse extends Response {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2210197639875241944L;
    private List<UserNode> _users;
    
    public List<UserNode> getUsers() {
        return _users;
    }

    public void setUsers(List<UserNode> users) {
        _users = users;
    }
}
