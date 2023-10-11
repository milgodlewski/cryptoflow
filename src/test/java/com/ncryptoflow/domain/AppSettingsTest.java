package com.ncryptoflow.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ncryptoflow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppSettings.class);
        AppSettings appSettings1 = new AppSettings();
        appSettings1.setId(1L);
        AppSettings appSettings2 = new AppSettings();
        appSettings2.setId(appSettings1.getId());
        assertThat(appSettings1).isEqualTo(appSettings2);
        appSettings2.setId(2L);
        assertThat(appSettings1).isNotEqualTo(appSettings2);
        appSettings1.setId(null);
        assertThat(appSettings1).isNotEqualTo(appSettings2);
    }
}
