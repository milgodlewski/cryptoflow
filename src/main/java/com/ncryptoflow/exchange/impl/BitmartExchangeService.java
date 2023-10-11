package com.ncryptoflow.exchange.impl;

import com.ncryptoflow.exchange.ExchangeService;
import com.ncryptoflow.exchange.domain.TradeOffer;
import java.util.List;

/**
 * Bitmart ExchangeService implementation.
 * */
public class BitmartExchangeService implements ExchangeService {

    @Override
    public List<String> getAllCurrencyPairs() {
        return null;
    }

    @Override
    public List<TradeOffer> fetchTradeOffers(String currencyPair) {
        return null;
    }
}
