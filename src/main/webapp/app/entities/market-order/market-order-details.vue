<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="marketOrder">
        <h2 class="jh-entity-heading" data-cy="marketOrderDetailsHeading"><span>Market Order</span> {{ marketOrder.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Type</span>
          </dt>
          <dd>
            <span>{{ marketOrder.type }}</span>
          </dd>
          <dt>
            <span>Price</span>
          </dt>
          <dd>
            <span>{{ marketOrder.price }}</span>
          </dd>
          <dt>
            <span>Amount</span>
          </dt>
          <dd>
            <span>{{ marketOrder.amount }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link
          v-if="marketOrder.id"
          :to="{ name: 'MarketOrderEdit', params: { marketOrderId: marketOrder.id } }"
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

<script lang="ts" src="./market-order-details.component.ts"></script>
