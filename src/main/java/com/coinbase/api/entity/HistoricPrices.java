package com.coinbase.api.entity;

import java.io.Serializable;
import java.util.List;

public class HistoricPrices implements Serializable {

    private static final long serialVersionUID = 9026218530572888542L;

    public enum Period {
        HOUR("hour"),
        DAY("day"),
        WEEK("week"),
        MONTH("month"),
        YEAR("year"),
        ALL("all");

        String value;

        Period(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private String _currency;
    private List<HistoricPrice> _prices;

    public String getCurrency() {
        return _currency;
    }

    public void setCurrency(String currency) {
        this._currency = currency;
    }

    public List<HistoricPrice> getPrices() {
        return _prices;
    }

    public void setPrices(List<HistoricPrice> prices) {
        _prices = prices;
    }
}
