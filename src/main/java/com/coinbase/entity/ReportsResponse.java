package com.coinbase.entity;

import java.util.List;

import com.coinbase.deserializer.ReportsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReportsResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -72799672257529179L;
    private List<com.coinbase.entity.Report> _reports;
    
    public List<com.coinbase.entity.Report> getReports() {
	return _reports;
    }

    @JsonDeserialize(converter=ReportsLifter.class)
    public void setReports(List<com.coinbase.entity.Report> reports) {
	_reports = reports;
    }
}
