package com.coinbase.v2.models.transfers;

import com.coinbase.v2.models.errors.ErrorBody;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class TransferError {
    @SerializedName("errors")
    @Expose
    private List<ErrorBody> errors = new ArrayList<>();

    @SerializedName("data")
    @Expose
    private Data data;

    public List<ErrorBody> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorBody> errors) {
        this.errors = errors;
    }

    /**
     *
     * @return
     *     The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     *     The data
     */
    public void setData(Data data) {
        this.data = data;
    }
}
