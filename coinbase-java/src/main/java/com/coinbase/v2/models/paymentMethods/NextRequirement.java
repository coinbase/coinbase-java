
package com.coinbase.v2.models.paymentMethods;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class NextRequirement {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("volume")
    @Expose
    private Amount volume;
    @SerializedName("completion_time")
    @Expose
    private String completionTime;
    @SerializedName("amount_remaining")
    @Expose
    private Amount amountRemaining;

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The volume
     */
    public Amount getVolume() {
        return volume;
    }

    /**
     * 
     * @param volume
     *     The volume
     */
    public void setVolume(Amount volume) {
        this.volume = volume;
    }

    /**
     * 
     * @return
     *     The completionTime
     */
    public String getCompletionTime() {
        return completionTime;
    }

    /**
     * 
     * @param completionTime
     *     The completion_time
     */
    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * 
     * @return
     *     The amountRemaining
     */
    public Amount getAmountRemaining() {
        return amountRemaining;
    }

    /**
     * 
     * @param amountRemaining
     *     The amount_remaining
     */
    public void setAmountRemaining(Amount amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

}
