<template>
  <div>
    <h2 id="page-heading" data-cy="ArbitrageOpportunityHeading">
      <span id="arbitrage-opportunity-heading">Arbitrage Opportunities</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'ArbitrageOpportunityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-arbitrage-opportunity"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Arbitrage Opportunity</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && arbitrageOpportunities && arbitrageOpportunities.length === 0">
      <span>No Arbitrage Opportunities found</span>
    </div>
    <div class="table-responsive" v-if="arbitrageOpportunities && arbitrageOpportunities.length > 0">
      <table class="table table-striped" aria-describedby="arbitrageOpportunities">
        <thead>
          <tr>
            <th scope="row"><span>ID</span></th>
            <th scope="row"><span>Base Currency</span></th>
            <th scope="row"><span>Target Currency</span></th>
            <th scope="row"><span>Source Exchange</span></th>
            <th scope="row"><span>Target Exchange</span></th>
            <th scope="row"><span>Opportunity Percentage</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="arbitrageOpportunity in arbitrageOpportunities" :key="arbitrageOpportunity.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ArbitrageOpportunityView', params: { arbitrageOpportunityId: arbitrageOpportunity.id } }">{{
                arbitrageOpportunity.id
              }}</router-link>
            </td>
            <td>{{ arbitrageOpportunity.baseCurrency }}</td>
            <td>{{ arbitrageOpportunity.targetCurrency }}</td>
            <td>{{ arbitrageOpportunity.sourceExchange }}</td>
            <td>{{ arbitrageOpportunity.targetExchange }}</td>
            <td>{{ arbitrageOpportunity.opportunityPercentage }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ArbitrageOpportunityView', params: { arbitrageOpportunityId: arbitrageOpportunity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ArbitrageOpportunityEdit', params: { arbitrageOpportunityId: arbitrageOpportunity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(arbitrageOpportunity)"
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
        <span id="ncryptoflowApp.arbitrageOpportunity.delete.question" data-cy="arbitrageOpportunityDeleteDialogHeading"
          >Confirm delete operation</span
        >
      </template>
      <div class="modal-body">
        <p id="jhi-delete-arbitrageOpportunity-heading">Are you sure you want to delete Arbitrage Opportunity {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-arbitrageOpportunity"
            data-cy="entityConfirmDeleteButton"
            v-on:click="removeArbitrageOpportunity()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./arbitrage-opportunity.component.ts"></script>
