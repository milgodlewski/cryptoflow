<template>
  <div>
    <b-dropdown-item to="/app-settings">
      <font-awesome-icon icon="asterisk" />
      <span>App Settings</span>
    </b-dropdown-item>
    <b-dropdown-item to="/arbitrage-opportunity">
      <font-awesome-icon icon="asterisk" />
      <span>Arbitrage Opportunity</span>
    </b-dropdown-item>
    <b-dropdown-item to="/currency-pair">
      <font-awesome-icon icon="asterisk" />
      <span>Currency Pair</span>
    </b-dropdown-item>
    <b-dropdown-item to="/currency-pair-mapping">
      <font-awesome-icon icon="asterisk" />
      <span>Currency Pair Mapping</span>
    </b-dropdown-item>
    <b-dropdown-item to="/exchange">
      <font-awesome-icon icon="asterisk" />
      <span>Exchange</span>
    </b-dropdown-item>
    <b-dropdown-item to="/market-order">
      <font-awesome-icon icon="asterisk" />
      <span>Market Order</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
