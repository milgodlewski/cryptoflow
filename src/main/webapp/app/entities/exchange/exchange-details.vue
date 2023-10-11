<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="exchange">
        <h2 class="jh-entity-heading" data-cy="exchangeDetailsHeading"><span>Exchange</span> {{ exchange.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Name</span>
          </dt>
          <dd>
            <span>{{ exchange.name }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link v-if="exchange.id" :to="{ name: 'ExchangeEdit', params: { exchangeId: exchange.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./exchange-details.component.ts"></script>
