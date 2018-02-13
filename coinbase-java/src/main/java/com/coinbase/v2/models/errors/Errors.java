package com.coinbase.v2.models.errors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Errors {
    @SerializedName("errors")
    @Expose
    private List<ErrorBody> errors = new ArrayList<>();;

    public List<ErrorBody> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorBody> errors) {
        this.errors = errors;
    }
}
