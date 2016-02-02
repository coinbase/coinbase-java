package com.coinbase.exception;

public class TwoFactorIncorrectException extends CoinbaseException {
    
    public TwoFactorIncorrectException() {
        super("The two factor token is incorrect or expired");
    }
    
}
