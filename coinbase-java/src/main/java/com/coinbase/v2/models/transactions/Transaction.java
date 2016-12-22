package com.coinbase.v2.models.transactions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Transaction {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("user_warnings")
    @Expose
    private List<UserWarning> userWarnings = new ArrayList<UserWarning>();

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     *
     * @return
     *     The userWarnings
     */
    public List<UserWarning> getUserWarnings() {
        return userWarnings;
    }

    /**
     *
     * @param userWarnings
     *     The user_warnings
     */
    public void setUserWarnings(List<UserWarning> userWarnings) {
        this.userWarnings = userWarnings;
    }
}