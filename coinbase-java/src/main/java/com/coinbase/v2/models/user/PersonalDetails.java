
package com.coinbase.v2.models.user;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PersonalDetails {

    @SerializedName("date_of_birth")
    @Expose
    private DateOfBirth dateOfBirth;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("legal_name")
    @Expose
    private LegalName legalName;

    /**
     * 
     * @return
     *     The dateOfBirth
     */
    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * 
     * @param dateOfBirth
     *     The date_of_birth
     */
    public void setDateOfBirth(DateOfBirth dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * 
     * @return
     *     The address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * 
     * @return
     *     The legalName
     */
    public LegalName getLegalName() {
        return legalName;
    }

    /**
     * 
     * @param legalName
     *     The legal_name
     */
    public void setLegalName(LegalName legalName) {
        this.legalName = legalName;
    }

}
