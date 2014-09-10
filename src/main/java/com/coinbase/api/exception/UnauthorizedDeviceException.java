package com.coinbase.api.exception;

public class UnauthorizedDeviceException extends CoinbaseException {

    public UnauthorizedDeviceException() {
        super("Unconfirmed Device");
    }
    
}
