package com.coinbase.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Pagination {

    @SerializedName("ending_before")
    @Expose
    private String endingBefore;
    @SerializedName("starting_after")
    @Expose
    private String startingAfter;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("previous_uri")
    @Expose
    private String previousUri;
    @SerializedName("next_uri")
    @Expose
    private String nextUri;

    /**
     *
     * @return
     * The endingBefore
     */
    public String getEndingBefore() {
        return endingBefore;
    }

    /**
     *
     * @param endingBefore
     * The ending_before
     */
    public void setEndingBefore(String endingBefore) {
        this.endingBefore = endingBefore;
    }

    /**
     *
     * @return
     * The startingAfter
     */
    public String getStartingAfter() {
        return startingAfter;
    }

    /**
     *
     * @param startingAfter
     * The starting_after
     */
    public void setStartingAfter(String startingAfter) {
        this.startingAfter = startingAfter;
    }

    /**
     *
     * @return
     * The limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     *
     * @param limit
     * The limit
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     *
     * @return
     * The order
     */
    public String getOrder() {
        return order;
    }

    /**
     *
     * @param order
     * The order
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     *
     * @return
     * The previousUri
     */
    public String getPreviousUri() {
        return previousUri;
    }

    /**
     *
     * @param previousUri
     * The previous_uri
     */
    public void setPreviousUri(String previousUri) {
        this.previousUri = previousUri;
    }

    /**
     *
     * @return
     * The nextUri
     */
    public String getNextUri() {
        return nextUri;
    }

    /**
     *
     * @param nextUri
     * The next_uri
     */
    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }

}