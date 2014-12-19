package com.coinbase.api.entity;

import java.util.List;

import com.coinbase.api.deserializer.ReportsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReportsResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -72799672257529179L;
    private List<Report> _reports;
    
    public List<Report> getReports() {
	return _reports;
    }

    @JsonDeserialize(converter=ReportsLifter.class)
    public void setReports(List<Report> reports) {
	_reports = reports;
    }
}
