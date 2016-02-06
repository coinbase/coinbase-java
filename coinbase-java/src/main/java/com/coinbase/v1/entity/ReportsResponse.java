package com.coinbase.v1.entity;

import com.coinbase.v1.deserializer.ReportsLifter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

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
