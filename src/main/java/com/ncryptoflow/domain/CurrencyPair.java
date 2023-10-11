package com.ncryptoflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CurrencyPair.
 */
@Table("currency_pair")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CurrencyPair implements Serializable {

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
    @Column("exchange_rate")
    private Double exchangeRate;

    @Transient
    @JsonIgnoreProperties(value = { "currencyPairs" }, allowSetters = true)
    private Set<Exchange> exchanges = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "buyCurrencyPairs", "sellCurrencyPairs" }, allowSetters = true)
    private Set<MarketOrder> buyOrders = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "buyCurrencyPairs", "sellCurrencyPairs" }, allowSetters = true)
    private Set<MarketOrder> sellOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CurrencyPair id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaseCurrency() {
        return this.baseCurrency;
    }

    public CurrencyPair baseCurrency(String baseCurrency) {
        this.setBaseCurrency(baseCurrency);
        return this;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return this.targetCurrency;
    }

    public CurrencyPair targetCurrency(String targetCurrency) {
        this.setTargetCurrency(targetCurrency);
        return this;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Double getExchangeRate() {
        return this.exchangeRate;
    }

    public CurrencyPair exchangeRate(Double exchangeRate) {
        this.setExchangeRate(exchangeRate);
        return this;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Set<Exchange> getExchanges() {
        return this.exchanges;
    }

    public void setExchanges(Set<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    public CurrencyPair exchanges(Set<Exchange> exchanges) {
        this.setExchanges(exchanges);
        return this;
    }

    public CurrencyPair addExchanges(Exchange exchange) {
        this.exchanges.add(exchange);
        exchange.getCurrencyPairs().add(this);
        return this;
    }

    public CurrencyPair removeExchanges(Exchange exchange) {
        this.exchanges.remove(exchange);
        exchange.getCurrencyPairs().remove(this);
        return this;
    }

    public Set<MarketOrder> getBuyOrders() {
        return this.buyOrders;
    }

    public void setBuyOrders(Set<MarketOrder> marketOrders) {
        this.buyOrders = marketOrders;
    }

    public CurrencyPair buyOrders(Set<MarketOrder> marketOrders) {
        this.setBuyOrders(marketOrders);
        return this;
    }

    public CurrencyPair addBuyOrders(MarketOrder marketOrder) {
        this.buyOrders.add(marketOrder);
        marketOrder.getBuyCurrencyPairs().add(this);
        return this;
    }

    public CurrencyPair removeBuyOrders(MarketOrder marketOrder) {
        this.buyOrders.remove(marketOrder);
        marketOrder.getBuyCurrencyPairs().remove(this);
        return this;
    }

    public Set<MarketOrder> getSellOrders() {
        return this.sellOrders;
    }

    public void setSellOrders(Set<MarketOrder> marketOrders) {
        this.sellOrders = marketOrders;
    }

    public CurrencyPair sellOrders(Set<MarketOrder> marketOrders) {
        this.setSellOrders(marketOrders);
        return this;
    }

    public CurrencyPair addSellOrders(MarketOrder marketOrder) {
        this.sellOrders.add(marketOrder);
        marketOrder.getSellCurrencyPairs().add(this);
        return this;
    }

    public CurrencyPair removeSellOrders(MarketOrder marketOrder) {
        this.sellOrders.remove(marketOrder);
        marketOrder.getSellCurrencyPairs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyPair)) {
            return false;
        }
        return id != null && id.equals(((CurrencyPair) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyPair{" +
            "id=" + getId() +
            ", baseCurrency='" + getBaseCurrency() + "'" +
            ", targetCurrency='" + getTargetCurrency() + "'" +
            ", exchangeRate=" + getExchangeRate() +
            "}";
    }
}
