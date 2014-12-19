package com.coinbase.api.exception;

public class TwoFactorRequiredException extends CoinbaseException {
    
    public TwoFactorRequiredException() {
        super("A two factor token is required");
    }
    
}
