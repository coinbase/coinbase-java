package com.coinbase.entity;

public class ReportResponse extends Response {
    /**
     * 
     */
    private static final long serialVersionUID = -4493964618141896877L;
    private com.coinbase.entity.Report _report;

    public com.coinbase.entity.Report getReport() {
        return _report;
    }

    public void setReport(com.coinbase.entity.Report report) {
        _report = report;
    }
}
