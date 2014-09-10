package com.coinbase.api.entity;

public class OAuthTokensResponse extends Response {
    private static final long serialVersionUID = -6431207976215527058L;
    
    private String _accessToken;
    private String _refreshToken;
    private String _tokenType;
    private String _scope;
    private Integer _expireIn;
    
    public String getAccessToken() {
        return _accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        _accessToken = accessToken;
    }
    
    public String getRefreshToken() {
        return _refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        _refreshToken = refreshToken;
    }
    
    public String getTokenType() {
        return _tokenType;
    }
    
    public void setTokenType(String tokenType) {
        _tokenType = tokenType;
    }
    
    public String getScope() {
        return _scope;
    }
    
    public void setScope(String scope) {
        _scope = scope;
    }
    
    public Integer getExpireIn() {
        return _expireIn;
    }
    
    public void setExpireIn(Integer expireIn) {
        _expireIn = expireIn;
    }
}
