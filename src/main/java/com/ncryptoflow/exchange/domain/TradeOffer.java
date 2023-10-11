package com.ncryptoflow.exchange.domain;

/**
 * TradeOffer class to handle buy and sell orders for @CurrencyPair.
 * */
public class TradeOffer {

    private String currencyPair;
    private double buyPrice;
    private double sellPrice;

    public TradeOffer(String currencyPair, double buyPrice, double sellPrice) {
        this.currencyPair = currencyPair;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }
}
