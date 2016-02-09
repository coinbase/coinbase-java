package com.coinbase.v1.exception;

public class UnauthorizedDeviceException extends CoinbaseException {

    public UnauthorizedDeviceException() {
        super("Unconfirmed Device");
    }
    
}
