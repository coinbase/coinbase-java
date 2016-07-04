
package com.coinbase.v2.models.paymentMethods;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Pagination {

    @SerializedName("ending_before")
    @Expose
    private Object endingBefore;
    @SerializedName("starting_after")
    @Expose
    private Object startingAfter;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("previous_uri")
    @Expose
    private Object previousUri;
    @SerializedName("next_uri")
    @Expose
    private Object nextUri;

    /**
     * 
     * @return
     *     The endingBefore
     */
    public Object getEndingBefore() {
        return endingBefore;
    }

    /**
     * 
     * @param endingBefore
     *     The ending_before
     */
    public void setEndingBefore(Object endingBefore) {
        this.endingBefore = endingBefore;
    }

    /**
     * 
     * @return
     *     The startingAfter
     */
    public Object getStartingAfter() {
        return startingAfter;
    }

    /**
     * 
     * @param startingAfter
     *     The starting_after
     */
    public void setStartingAfter(Object startingAfter) {
        this.startingAfter = startingAfter;
    }

    /**
     * 
     * @return
     *     The order
     */
    public String getOrder() {
        return order;
    }

    /**
     * 
     * @param order
     *     The order
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * 
     * @return
     *     The limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * 
     * @param limit
     *     The limit
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * 
     * @return
     *     The previousUri
     */
    public Object getPreviousUri() {
        return previousUri;
    }

    /**
     * 
     * @param previousUri
     *     The previous_uri
     */
    public void setPreviousUri(Object previousUri) {
        this.previousUri = previousUri;
    }

    /**
     * 
     * @return
     *     The nextUri
     */
    public Object getNextUri() {
        return nextUri;
    }

    /**
     * 
     * @param nextUri
     *     The next_uri
     */
    public void setNextUri(Object nextUri) {
        this.nextUri = nextUri;
    }

}
