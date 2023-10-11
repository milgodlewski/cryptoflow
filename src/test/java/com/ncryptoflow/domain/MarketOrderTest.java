package com.ncryptoflow.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ncryptoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarketOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketOrder.class);
        MarketOrder marketOrder1 = new MarketOrder();
        marketOrder1.setId(1L);
        MarketOrder marketOrder2 = new MarketOrder();
        marketOrder2.setId(marketOrder1.getId());
        assertThat(marketOrder1).isEqualTo(marketOrder2);
        marketOrder2.setId(2L);
        assertThat(marketOrder1).isNotEqualTo(marketOrder2);
        marketOrder1.setId(null);
        assertThat(marketOrder1).isNotEqualTo(marketOrder2);
    }
}
