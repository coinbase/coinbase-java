
package com.coinbase.v2.models.paymentMethods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class InstantBuy {

    @SerializedName("period_in_days")
    @Expose
    private Integer periodInDays;
    @SerializedName("total")
    @Expose
    private Amount total;
    @SerializedName("remaining")
    @Expose
    private Amount remaining;
    @SerializedName("next_requirement")
    @Expose
    private NextRequirement nextRequirement;

    /**
     * 
     * @return
     *     The periodInDays
     */
    public Integer getPeriodInDays() {
        return periodInDays;
    }

    /**
     * 
     * @param periodInDays
     *     The period_in_days
     */
    public void setPeriodInDays(Integer periodInDays) {
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


}
