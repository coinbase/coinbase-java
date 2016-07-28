package com.coinbase.v2.models.transfers;

/**
 * Created by JaneChung on 7/28/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Payload {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private String value;

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     *     The value
     */
    public void setValue(String value) {
        this.value = value;
    }

}