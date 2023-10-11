<template>
  <div>
    <h2 id="page-heading" data-cy="CurrencyPairMappingHeading">
      <span id="currency-pair-mapping-heading">Currency Pair Mappings</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'CurrencyPairMappingCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-currency-pair-mapping"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Currency Pair Mapping</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && currencyPairMappings && currencyPairMappings.length === 0">
      <span>No Currency Pair Mappings found</span>
    </div>
    <div class="table-responsive" v-if="currencyPairMappings && currencyPairMappings.length > 0">
      <table class="table table-striped" aria-describedby="currencyPairMappings">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Base Currency</span></th>
            <th scope="row"><span>Target Currency</span></th>
            <th scope="row"><span>Source Exchange</span></th>
            <th scope="row"><span>Target Exchange</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="currencyPairMapping in currencyPairMappings" :key="currencyPairMapping.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CurrencyPairMappingView', params: { currencyPairMappingId: currencyPairMapping.id } }">{{
                currencyPairMapping.id
              }}</router-link>
            </td>
            <td>{{ currencyPairMapping.baseCurrency }}</td>
            <td>{{ currencyPairMapping.targetCurrency }}</td>
            <td>{{ currencyPairMapping.sourceExchange }}</td>
            <td>{{ currencyPairMapping.targetExchange }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'CurrencyPairMappingView', params: { currencyPairMappingId: currencyPairMapping.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'CurrencyPairMappingEdit', params: { currencyPairMappingId: currencyPairMapping.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(currencyPairMapping)"
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
        <span id="ncryptoflowApp.currencyPairMapping.delete.question" data-cy="currencyPairMappingDeleteDialogHeading"
          >Confirm delete operation</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-currencyPairMapping-heading">Are you sure you want to delete Currency Pair Mapping {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-currencyPairMapping"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeCurrencyPairMapping()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./currency-pair-mapping.component.ts"></script>
