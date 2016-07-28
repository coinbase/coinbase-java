package com.coinbase.v2.models.transfers;

/**
 * Created by JaneChung on 7/28/16.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Secure3dVerification {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("payload")
    @Expose
    private List<Payload> payload = new ArrayList<Payload>();

    /**
     *
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     *     The payload
     */
    public List<Payload> getPayload() {
        return payload;
    }

    /**
     *
     * @param payload
     *     The payload
     */
    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }

}
