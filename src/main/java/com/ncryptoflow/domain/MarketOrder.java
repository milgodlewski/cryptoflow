package com.ncryptoflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ncryptoflow.domain.enumeration.OrderType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A MarketOrder.
 */
@Table("market_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MarketOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type")
    private OrderType type;

    @NotNull(message = "must not be null")
    @Column("price")
    private Double price;

    @NotNull(message = "must not be null")
    @Column("amount")
    private Double amount;

    @Transient
    @JsonIgnoreProperties(value = { "exchanges", "buyOrders", "sellOrders" }, allowSetters = true)
    private Set<CurrencyPair> buyCurrencyPairs = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "exchanges", "buyOrders", "sellOrders" }, allowSetters = true)
    private Set<CurrencyPair> sellCurrencyPairs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MarketOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderType getType() {
        return this.type;
    }

    public MarketOrder type(OrderType type) {
        this.setType(type);
        return this;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public Double getPrice() {
        return this.price;
    }

    public MarketOrder price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return this.amount;
    }

    public MarketOrder amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Set<CurrencyPair> getBuyCurrencyPairs() {
        return this.buyCurrencyPairs;
    }

    public void setBuyCurrencyPairs(Set<CurrencyPair> currencyPairs) {
        if (this.buyCurrencyPairs != null) {
            this.buyCurrencyPairs.forEach(i -> i.removeBuyOrders(this));
        }
        if (currencyPairs != null) {
            currencyPairs.forEach(i -> i.addBuyOrders(this));
        }
        this.buyCurrencyPairs = currencyPairs;
    }

    public MarketOrder buyCurrencyPairs(Set<CurrencyPair> currencyPairs) {
        this.setBuyCurrencyPairs(currencyPairs);
        return this;
    }

    public MarketOrder addBuyCurrencyPairs(CurrencyPair currencyPair) {
        this.buyCurrencyPairs.add(currencyPair);
        currencyPair.getBuyOrders().add(this);
        return this;
    }

    public MarketOrder removeBuyCurrencyPairs(CurrencyPair currencyPair) {
        this.buyCurrencyPairs.remove(currencyPair);
        currencyPair.getBuyOrders().remove(this);
        return this;
    }

    public Set<CurrencyPair> getSellCurrencyPairs() {
        return this.sellCurrencyPairs;
    }

    public void setSellCurrencyPairs(Set<CurrencyPair> currencyPairs) {
        if (this.sellCurrencyPairs != null) {
            this.sellCurrencyPairs.forEach(i -> i.removeSellOrders(this));
        }
        if (currencyPairs != null) {
            currencyPairs.forEach(i -> i.addSellOrders(this));
        }
        this.sellCurrencyPairs = currencyPairs;
    }

    public MarketOrder sellCurrencyPairs(Set<CurrencyPair> currencyPairs) {
        this.setSellCurrencyPairs(currencyPairs);
        return this;
    }

    public MarketOrder addSellCurrencyPairs(CurrencyPair currencyPair) {
        this.sellCurrencyPairs.add(currencyPair);
        currencyPair.getSellOrders().add(this);
        return this;
    }

    public MarketOrder removeSellCurrencyPairs(CurrencyPair currencyPair) {
        this.sellCurrencyPairs.remove(currencyPair);
        currencyPair.getSellOrders().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarketOrder)) {
            return false;
        }
        return id != null && id.equals(((MarketOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarketOrder{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", price=" + getPrice() +
            ", amount=" + getAmount() +
            "}";
    }
}
