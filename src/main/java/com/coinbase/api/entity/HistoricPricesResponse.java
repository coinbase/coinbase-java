package com.coinbase.api.entity;

public class HistoricPricesResponse extends Response {

    private static final long serialVersionUID = -5415669981469295597L;

    private HistoricPrices _data;

    public HistoricPrices getData() {
        return _data;
    }

    public void setData(HistoricPrices historicPrices) {
        _data = historicPrices;
    }
}
