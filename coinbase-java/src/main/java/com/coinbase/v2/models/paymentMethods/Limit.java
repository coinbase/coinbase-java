
package com.coinbase.v2.models.paymentMethods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Limit {

    @SerializedName("period_in_days")
    @Expose
    private Float periodInDays;
    @SerializedName("total")
    @Expose
    private Amount total;
    @SerializedName("remaining")
    @Expose
    private Amount remaining;
    @SerializedName("next_requirement")
    @Expose
    private NextRequirement nextRequirement;
    @SerializedName("description")
    @Expose
    private String description;

    /**
     * 
     * @return
     *     The periodInDays
     */
    public Float getPeriodInDays() {
        return periodInDays;
    }

    /**
     * 
     * @param periodInDays
     *     The period_in_days
     */
    public void setPeriodInDays(Float periodInDays) {
        this.periodInDays = periodInDays;
    }

    /**
     * 
     * @return
     *     The total
     */
    public Amount getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(Amount total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The remaining
     */
    public Amount getRemaining() {
        return remaining;
    }

    /**
     * 
     * @param remaining
     *     The remaining
     */
    public void setRemaining(Amount remaining) {
        this.remaining = remaining;
    }

    /**
     *
     * @return
     *     The nextRequirement
     */
    public NextRequirement getNextRequirement() {
        return nextRequirement;
    }

    /**
     *
     * @param nextRequirement
     *     The next_requirement
     */
    public void setNextRequirement(NextRequirement nextRequirement) {
        this.nextRequirement = nextRequirement;
    }

    /**
     *
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param periodInDays
     *     The period_in_days
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
