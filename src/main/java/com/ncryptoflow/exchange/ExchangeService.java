package com.ncryptoflow.exchange;

import com.ncryptoflow.exchange.domain.TradeOffer;
import java.util.List;

public interface ExchangeService {
    List<String> getAllCurrencyPairs();
    List<TradeOffer> fetchTradeOffers(String currencyPair);
}
