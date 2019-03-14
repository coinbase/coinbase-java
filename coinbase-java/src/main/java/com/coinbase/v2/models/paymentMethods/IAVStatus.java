package com.coinbase.v2.models.paymentMethods;

import com.google.gson.annotations.SerializedName;

public enum IAVStatus {
    @SerializedName("plaid_success")
    PLAID_SUCCESS(),

    @SerializedName("plaid_complete")
    PLAID_COMPLETE(),

    @SerializedName("plaid_revoked")
    PLAID_REVOKED(),

    UNKNOWN();

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
