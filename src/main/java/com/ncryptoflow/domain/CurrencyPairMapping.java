package com.ncryptoflow.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CurrencyPairMapping.
 */
@Table("currency_pair_mapping")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurrencyPairMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("base_currency")
    private String baseCurrency;

    @NotNull(message = "must not be null")
    @Column("target_currency")
    private String targetCurrency;

    @NotNull(message = "must not be null")
    @Column("source_exchange")
    private String sourceExchange;

    @NotNull(message = "must not be null")
    @Column("target_exchange")
    private String targetExchange;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CurrencyPairMapping id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseCurrency() {
        return this.baseCurrency;
    }

    public CurrencyPairMapping baseCurrency(String baseCurrency) {
        this.setBaseCurrency(baseCurrency);
        return this;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return this.targetCurrency;
    }

    public CurrencyPairMapping targetCurrency(String targetCurrency) {
        this.setTargetCurrency(targetCurrency);
        return this;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getSourceExchange() {
        return this.sourceExchange;
    }

    public CurrencyPairMapping sourceExchange(String sourceExchange) {
        this.setSourceExchange(sourceExchange);
        return this;
    }

    public void setSourceExchange(String sourceExchange) {
        this.sourceExchange = sourceExchange;
    }

    public String getTargetExchange() {
        return this.targetExchange;
    }

    public CurrencyPairMapping targetExchange(String targetExchange) {
        this.setTargetExchange(targetExchange);
        return this;
    }

    public void setTargetExchange(String targetExchange) {
        this.targetExchange = targetExchange;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyPairMapping)) {
            return false;
        }
        return id != null && id.equals(((CurrencyPairMapping) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyPairMapping{" +
            "id=" + getId() +
            ", baseCurrency='" + getBaseCurrency() + "'" +
            ", targetCurrency='" + getTargetCurrency() + "'" +
            ", sourceExchange='" + getSourceExchange() + "'" +
            ", targetExchange='" + getTargetExchange() + "'" +
            "}";
    }
}
