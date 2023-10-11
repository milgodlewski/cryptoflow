<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="currencyPair">
        <h2 class="jh-entity-heading" data-cy="currencyPairDetailsHeading"><span>Currency Pair</span> {{ currencyPair.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Base Currency</span>
          </dt>
          <dd>
            <span>{{ currencyPair.baseCurrency }}</span>
          </dd>
          <dt>
            <span>Target Currency</span>
          </dt>
          <dd>
            <span>{{ currencyPair.targetCurrency }}</span>
          </dd>
          <dt>
            <span>Exchange Rate</span>
          </dt>
          <dd>
            <span>{{ currencyPair.exchangeRate }}</span>
          </dd>
          <dt>
            <span>Exchanges</span>
          </dt>
          <dd>
            <span v-for="(exchanges, i) in currencyPair.exchanges" :key="exchanges.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ExchangeView', params: { exchangeId: exchanges.id } }">{{ exchanges.id }}</router-link>
            </span>
          </dd>
          <dt>
            <span>Buy Orders</span>
          </dt>
          <dd>
            <span v-for="(buyOrders, i) in currencyPair.buyOrders" :key="buyOrders.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'MarketOrderView', params: { marketOrderId: buyOrders.id } }">{{ buyOrders.id }}</router-link>
            </span>
          </dd>
          <dt>
            <span>Sell Orders</span>
          </dt>
          <dd>
            <span v-for="(sellOrders, i) in currencyPair.sellOrders" :key="sellOrders.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'MarketOrderView', params: { marketOrderId: sellOrders.id } }">{{ sellOrders.id }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="currencyPair.id"
          :to="{ name: 'CurrencyPairEdit', params: { currencyPairId: currencyPair.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./currency-pair-details.component.ts"></script>
