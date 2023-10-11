<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="arbitrageOpportunity">
        <h2 class="jh-entity-heading" data-cy="arbitrageOpportunityDetailsHeading">
          <span>Arbitrage Opportunity</span> {{ arbitrageOpportunity.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Base Currency</span>
          </dt>
          <dd>
            <span>{{ arbitrageOpportunity.baseCurrency }}</span>
          </dd>
          <dt>
            <span>Target Currency</span>
          </dt>
          <dd>
            <span>{{ arbitrageOpportunity.targetCurrency }}</span>
          </dd>
          <dt>
            <span>Source Exchange</span>
          </dt>
          <dd>
            <span>{{ arbitrageOpportunity.sourceExchange }}</span>
          </dd>
          <dt>
            <span>Target Exchange</span>
          </dt>
          <dd>
            <span>{{ arbitrageOpportunity.targetExchange }}</span>
          </dd>
          <dt>
            <span>Opportunity Percentage</span>
          </dt>
          <dd>
            <span>{{ arbitrageOpportunity.opportunityPercentage }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="arbitrageOpportunity.id"
          :to="{ name: 'ArbitrageOpportunityEdit', params: { arbitrageOpportunityId: arbitrageOpportunity.id } }"
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

<script lang="ts" src="./arbitrage-opportunity-details.component.ts"></script>
