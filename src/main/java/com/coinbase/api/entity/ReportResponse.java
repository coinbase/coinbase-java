package com.coinbase.api.entity;

public class ReportResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -4493964618141896877L;
    private Report _report;

    public Report getReport() {
        return _report;
    }

    public void setReport(Report report) {
        _report = report;
    }
}
