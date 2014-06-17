package com.coinbase.api.entity;

import java.util.List;

public class UsersResponse extends Response {
    
    private List<UserNode> _users;
    
    public List<UserNode> getUsers() {
        return _users;
    }

    public void setUsers(List<UserNode> users) {
        _users = users;
    }

}
