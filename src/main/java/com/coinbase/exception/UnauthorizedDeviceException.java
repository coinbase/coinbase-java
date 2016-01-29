package com.coinbase.exception;

public class UnauthorizedDeviceException extends CoinbaseException {

    public UnauthorizedDeviceException() {
        super("Unconfirmed Device");
    }
    
}
