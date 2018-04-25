package com.coinbase.v2.models.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OnboardingItem {

    @SerializedName("step")
    @Expose
    private String step;
    @SerializedName("required")
    @Expose
    private Boolean required;

    /**
     *
     * @return
     *     The name of the step to show
     */
    public String getStep() {
        return step;
    }

    /**
     *
     * @param step
     *     The name of the step to show
     */
    public void setStep(String step) {
        this.step = step;
    }

    /**
     *
     * @return
     *     {@code true} if this is required to show, else {@code false}
     */
    public Boolean getRequired() {
        return required;
    }

    /**
     *
     * @param required
     *     {@code true} if this is required to show, else {@code false}
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }
}
