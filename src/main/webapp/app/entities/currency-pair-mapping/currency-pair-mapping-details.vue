<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="currencyPairMapping">
        <h2 class="jh-entity-heading" data-cy="currencyPairMappingDetailsHeading">
          <span>Currency Pair Mapping</span> {{ currencyPairMapping.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Base Currency</span>
          </dt>
          <dd>
            <span>{{ currencyPairMapping.baseCurrency }}</span>
          </dd>
          <dt>
            <span>Target Currency</span>
          </dt>
          <dd>
            <span>{{ currencyPairMapping.targetCurrency }}</span>
          </dd>
          <dt>
            <span>Source Exchange</span>
          </dt>
          <dd>
            <span>{{ currencyPairMapping.sourceExchange }}</span>
          </dd>
          <dt>
            <span>Target Exchange</span>
          </dt>
          <dd>
            <span>{{ currencyPairMapping.targetExchange }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="currencyPairMapping.id"
          :to="{ name: 'CurrencyPairMappingEdit', params: { currencyPairMappingId: currencyPairMapping.id } }"
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

<script lang="ts" src="./currency-pair-mapping-details.component.ts"></script>
