package com.ncryptoflow.exchange;

import java.util.List;

/**
 * ArbitrageService class to count arbitrage.
 * */
public class ArbitrageService {

    private ExchangeService binanceService;
    private ExchangeService bitmartService;
    private CurrencyPairMappingService mappingService;

    public ArbitrageService(ExchangeService binanceService, ExchangeService bitmartService, CurrencyPairMappingService mappingService) {
        this.binanceService = binanceService;
        this.bitmartService = bitmartService;
        this.mappingService = mappingService;
    }

    public void initializeExchanges() {
        List<String> binancePairs = binanceService.getAllCurrencyPairs();
        List<String> bitmartPairs = bitmartService.getAllCurrencyPairs();
        mappingService.initializeMapping(binancePairs, bitmartPairs);
    }

    public void findArbitrageOpportunities() {}
}
