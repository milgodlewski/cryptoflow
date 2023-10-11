<template>
  <div>
    <h2 id="page-heading" data-cy="MarketOrderHeading">
      <span id="market-order-heading">Market Orders</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'MarketOrderCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-market-order"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Market Order</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && marketOrders && marketOrders.length === 0">
      <span>No Market Orders found</span>
    </div>
    <div class="table-responsive" v-if="marketOrders && marketOrders.length > 0">
      <table class="table table-striped" aria-describedby="marketOrders">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Type</span></th>
            <th scope="row"><span>Price</span></th>
            <th scope="row"><span>Amount</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="marketOrder in marketOrders" :key="marketOrder.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MarketOrderView', params: { marketOrderId: marketOrder.id } }">{{ marketOrder.id }}</router-link>
            </td>
            <td>{{ marketOrder.type }}</td>
            <td>{{ marketOrder.price }}</td>
            <td>{{ marketOrder.amount }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MarketOrderView', params: { marketOrderId: marketOrder.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MarketOrderEdit', params: { marketOrderId: marketOrder.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(marketOrder)"
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
        <span id="ncryptoflowApp.marketOrder.delete.question" data-cy="marketOrderDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-marketOrder-heading">Are you sure you want to delete Market Order {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-marketOrder"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeMarketOrder()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./market-order.component.ts"></script>
