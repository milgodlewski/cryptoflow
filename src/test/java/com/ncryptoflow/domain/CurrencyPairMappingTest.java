package com.ncryptoflow.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ncryptoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CurrencyPairMappingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyPairMapping.class);
        CurrencyPairMapping currencyPairMapping1 = new CurrencyPairMapping();
        currencyPairMapping1.setId(1L);
        CurrencyPairMapping currencyPairMapping2 = new CurrencyPairMapping();
        currencyPairMapping2.setId(currencyPairMapping1.getId());
        assertThat(currencyPairMapping1).isEqualTo(currencyPairMapping2);
        currencyPairMapping2.setId(2L);
        assertThat(currencyPairMapping1).isNotEqualTo(currencyPairMapping2);
        currencyPairMapping1.setId(null);
        assertThat(currencyPairMapping1).isNotEqualTo(currencyPairMapping2);
    }
}
