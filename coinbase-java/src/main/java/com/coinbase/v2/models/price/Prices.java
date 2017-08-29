package com.coinbase.v2.models.price;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Prices {
    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<Data>();

    /**
     *
     * @return
     * The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }
}
