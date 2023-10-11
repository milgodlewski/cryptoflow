<template>
  <div>
    <h2 id="page-heading" data-cy="CurrencyPairHeading">
      <span id="currency-pair-heading">Currency Pairs</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'CurrencyPairCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-currency-pair"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Currency Pair</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && currencyPairs && currencyPairs.length === 0">
      <span>No Currency Pairs found</span>
    </div>
    <div class="table-responsive" v-if="currencyPairs && currencyPairs.length > 0">
      <table class="table table-striped" aria-describedby="currencyPairs">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Base Currency</span></th>
            <th scope="row"><span>Target Currency</span></th>
            <th scope="row"><span>Exchange Rate</span></th>
            <th scope="row"><span>Exchanges</span></th>
            <th scope="row"><span>Buy Orders</span></th>
            <th scope="row"><span>Sell Orders</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="currencyPair in currencyPairs" :key="currencyPair.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CurrencyPairView', params: { currencyPairId: currencyPair.id } }">{{
                currencyPair.id
              }}</router-link>
            </td>
            <td>{{ currencyPair.baseCurrency }}</td>
            <td>{{ currencyPair.targetCurrency }}</td>
            <td>{{ currencyPair.exchangeRate }}</td>
            <td>
              <span v-for="(exchanges, i) in currencyPair.exchanges" :key="exchanges.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'ExchangeView', params: { exchangeId: exchanges.id } }">{{
                  exchanges.id
                }}</router-link>
              </span>
            </td>
            <td>
              <span v-for="(buyOrders, i) in currencyPair.buyOrders" :key="buyOrders.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'MarketOrderView', params: { marketOrderId: buyOrders.id } }">{{
                  buyOrders.id
                }}</router-link>
              </span>
            </td>
            <td>
              <span v-for="(sellOrders, i) in currencyPair.sellOrders" :key="sellOrders.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link class="form-control-static" :to="{ name: 'MarketOrderView', params: { marketOrderId: sellOrders.id } }">{{
                  sellOrders.id
                }}</router-link>
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CurrencyPairView', params: { currencyPairId: currencyPair.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CurrencyPairEdit', params: { currencyPairId: currencyPair.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(currencyPair)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="ncryptoflowApp.currencyPair.delete.question" data-cy="currencyPairDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-currencyPair-heading">Are you sure you want to delete Currency Pair {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-currencyPair"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeCurrencyPair()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./currency-pair.component.ts"></script>
