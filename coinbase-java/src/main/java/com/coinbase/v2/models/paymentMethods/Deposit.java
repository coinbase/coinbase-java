
package com.coinbase.v2.models.paymentMethods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Deposit {

    @SerializedName("period_in_days")
    @Expose
    private Integer periodInDays;
    @SerializedName("total")
    @Expose
    private Amount total;
    @SerializedName("remaining")
    @Expose
    private Amount remaining;

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

}
