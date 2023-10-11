package com.ncryptoflow.exchange;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class CurrencyPairMappingService {

    private Map<String, Set<String>> currencyPairMapping;

    public void initializeMapping(List<String> leftPairs, List<String> rightPairs) {}
}
