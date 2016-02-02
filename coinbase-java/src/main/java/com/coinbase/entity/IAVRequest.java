package com.coinbase.entity;

import com.coinbase.serializer.IAVRequestSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.HashMap;

@JsonSerialize(using = IAVRequestSerializer.class)
public class IAVRequest implements Serializable {

    private static final long serialVersionUID = 3286918689461549816L;

    private com.coinbase.entity.PaymentMethod.VerificationMethod _verificationMethod;
    private HashMap<String, String> _iavFields;

    public com.coinbase.entity.PaymentMethod.VerificationMethod getVerificationMethod() {
        return _verificationMethod;
    }

    public void setVerificationMethod(com.coinbase.entity.PaymentMethod.VerificationMethod verificationMethod) {
        this._verificationMethod = verificationMethod;
    }

    public void setIavFields(HashMap<String, String> iavFields) {
        this._iavFields = iavFields;
    }

    public HashMap<String, String> getIavFields() {
        return _iavFields;
    }
}
