package com.coinbase.v2.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Tiers {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("completed")
    @Expose
    private Integer completed;
    @SerializedName("completed_description")
    @Expose
    private String completedDescription;

    /**
     *
     * @return
     *     The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     *
     * @param total
     *     The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     *
     * @return
     *     The completed
     */
    public Integer getCompleted() {
        return completed;
    }

    /**
     *
     * @param completed
     *     The completed
     */
    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    /**
     *
     * @return
     *     The completedDescription
     */
    public String getCompletedDescription() {
        return completedDescription;
    }

    /**
     *
     * @param completedDescription
     *     The completed_description
     */
    public void setCompletedDescription(String completedDescription) {
        this.completedDescription = completedDescription;
    }

}
