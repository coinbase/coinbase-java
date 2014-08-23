package com.coinbase.api.exception;

public class UnauthorizedException extends CoinbaseException {

    public UnauthorizedException() {
        super();
    }
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
}
