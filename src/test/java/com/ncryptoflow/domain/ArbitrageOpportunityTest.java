package com.ncryptoflow.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ncryptoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArbitrageOpportunityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArbitrageOpportunity.class);
        ArbitrageOpportunity arbitrageOpportunity1 = new ArbitrageOpportunity();
        arbitrageOpportunity1.setId(1L);
        ArbitrageOpportunity arbitrageOpportunity2 = new ArbitrageOpportunity();
        arbitrageOpportunity2.setId(arbitrageOpportunity1.getId());
        assertThat(arbitrageOpportunity1).isEqualTo(arbitrageOpportunity2);
        arbitrageOpportunity2.setId(2L);
        assertThat(arbitrageOpportunity1).isNotEqualTo(arbitrageOpportunity2);
        arbitrageOpportunity1.setId(null);
        assertThat(arbitrageOpportunity1).isNotEqualTo(arbitrageOpportunity2);
    }
}
