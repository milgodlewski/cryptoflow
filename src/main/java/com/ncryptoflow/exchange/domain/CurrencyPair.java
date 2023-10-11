package com.ncryptoflow.exchange.domain;

/**
 * CurrencyPair class to handle currency trade pairs.
 * */
public class CurrencyPair {

    private String baseCurrency;
    private String targetCurrency;
    private double exchangeRate;

    public CurrencyPair(String baseCurrency, String targetCurrency, double exchangeRate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}
