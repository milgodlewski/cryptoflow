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
 * A Exchange.
 */
@Table("exchange")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Exchange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Transient
    @JsonIgnoreProperties(value = { "exchanges", "buyOrders", "sellOrders" }, allowSetters = true)
    private Set<CurrencyPair> currencyPairs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Exchange id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Exchange name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CurrencyPair> getCurrencyPairs() {
        return this.currencyPairs;
    }

    public void setCurrencyPairs(Set<CurrencyPair> currencyPairs) {
        if (this.currencyPairs != null) {
            this.currencyPairs.forEach(i -> i.removeExchanges(this));
        }
        if (currencyPairs != null) {
            currencyPairs.forEach(i -> i.addExchanges(this));
        }
        this.currencyPairs = currencyPairs;
    }

    public Exchange currencyPairs(Set<CurrencyPair> currencyPairs) {
        this.setCurrencyPairs(currencyPairs);
        return this;
    }

    public Exchange addCurrencyPairs(CurrencyPair currencyPair) {
        this.currencyPairs.add(currencyPair);
        currencyPair.getExchanges().add(this);
        return this;
    }

    public Exchange removeCurrencyPairs(CurrencyPair currencyPair) {
        this.currencyPairs.remove(currencyPair);
        currencyPair.getExchanges().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exchange)) {
            return false;
        }
        return id != null && id.equals(((Exchange) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exchange{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
