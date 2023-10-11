<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="ncryptoflowApp.arbitrageOpportunity.home.createOrEditLabel" data-cy="ArbitrageOpportunityCreateUpdateHeading">
          Create or edit a Arbitrage Opportunity
        </h2>
        <div>
          <div class="form-group" v-if="arbitrageOpportunity.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="arbitrageOpportunity.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="arbitrage-opportunity-baseCurrency">Base Currency</label>
            <input
              type="text"
              class="form-control"
              name="baseCurrency"
              id="arbitrage-opportunity-baseCurrency"
              data-cy="baseCurrency"
              :class="{ valid: !v$.baseCurrency.$invalid, invalid: v$.baseCurrency.$invalid }"
              v-model="v$.baseCurrency.$model"
              required
            />
            <div v-if="v$.baseCurrency.$anyDirty && v$.baseCurrency.$invalid">
              <small class="form-text text-danger" v-for="error of v$.baseCurrency.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="arbitrage-opportunity-targetCurrency">Target Currency</label>
            <input
              type="text"
              class="form-control"
              name="targetCurrency"
              id="arbitrage-opportunity-targetCurrency"
              data-cy="targetCurrency"
              :class="{ valid: !v$.targetCurrency.$invalid, invalid: v$.targetCurrency.$invalid }"
              v-model="v$.targetCurrency.$model"
              required
            />
            <div v-if="v$.targetCurrency.$anyDirty && v$.targetCurrency.$invalid">
              <small class="form-text text-danger" v-for="error of v$.targetCurrency.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="arbitrage-opportunity-sourceExchange">Source Exchange</label>
            <input
              type="text"
              class="form-control"
              name="sourceExchange"
              id="arbitrage-opportunity-sourceExchange"
              data-cy="sourceExchange"
              :class="{ valid: !v$.sourceExchange.$invalid, invalid: v$.sourceExchange.$invalid }"
              v-model="v$.sourceExchange.$model"
              required
            />
            <div v-if="v$.sourceExchange.$anyDirty && v$.sourceExchange.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sourceExchange.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="arbitrage-opportunity-targetExchange">Target Exchange</label>
            <input
              type="text"
              class="form-control"
              name="targetExchange"
              id="arbitrage-opportunity-targetExchange"
              data-cy="targetExchange"
              :class="{ valid: !v$.targetExchange.$invalid, invalid: v$.targetExchange.$invalid }"
              v-model="v$.targetExchange.$model"
              required
            />
            <div v-if="v$.targetExchange.$anyDirty && v$.targetExchange.$invalid">
              <small class="form-text text-danger" v-for="error of v$.targetExchange.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="arbitrage-opportunity-opportunityPercentage">Opportunity Percentage</label>
            <input
              type="number"
              class="form-control"
              name="opportunityPercentage"
              id="arbitrage-opportunity-opportunityPercentage"
              data-cy="opportunityPercentage"
              :class="{ valid: !v$.opportunityPercentage.$invalid, invalid: v$.opportunityPercentage.$invalid }"
              v-model.number="v$.opportunityPercentage.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./arbitrage-opportunity-update.component.ts"></script>
