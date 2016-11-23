
package com.coinbase.v2.models.supportedCurrencies;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SupportedCurrencies {

    @SerializedName("data")
    @Expose
    private List<Data> data = new ArrayList<Data>();

    /**
     * 
     * @return
     *     The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }

}
