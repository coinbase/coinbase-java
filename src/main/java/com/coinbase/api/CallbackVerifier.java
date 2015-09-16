package com.coinbase.api;


public interface CallbackVerifier {
    /**
     * Verify authenticity of merchant callback from Coinbase
     *
     *
     */
    boolean verifyCallback(String body, String signature);
}
