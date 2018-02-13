package com.coinbase.v2.models.errors;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ErrorBody {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("field")
    @Expose
    private String field;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
