<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="ncryptoflowApp.currencyPairMapping.home.createOrEditLabel" data-cy="CurrencyPairMappingCreateUpdateHeading">
          Create or edit a Currency Pair Mapping
        </h2>
        <div>
          <div class="form-group" v-if="currencyPairMapping.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="currencyPairMapping.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="currency-pair-mapping-baseCurrency">Base Currency</label>
            <input
              type="text"
              class="form-control"
              name="baseCurrency"
              id="currency-pair-mapping-baseCurrency"
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
            <label class="form-control-label" for="currency-pair-mapping-targetCurrency">Target Currency</label>
            <input
              type="text"
              class="form-control"
              name="targetCurrency"
              id="currency-pair-mapping-targetCurrency"
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
            <label class="form-control-label" for="currency-pair-mapping-sourceExchange">Source Exchange</label>
            <input
              type="text"
              class="form-control"
              name="sourceExchange"
              id="currency-pair-mapping-sourceExchange"
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
            <label class="form-control-label" for="currency-pair-mapping-targetExchange">Target Exchange</label>
            <input
              type="text"
              class="form-control"
              name="targetExchange"
              id="currency-pair-mapping-targetExchange"
              data-cy="targetExchange"
              :class="{ valid: !v$.targetExchange.$invalid, invalid: v$.targetExchange.$invalid }"
              v-model="v$.targetExchange.$model"
              required
            />
            <div v-if="v$.targetExchange.$anyDirty && v$.targetExchange.$invalid">
              <small class="form-text text-danger" v-for="error of v$.targetExchange.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
<script lang="ts" src="./currency-pair-mapping-update.component.ts"></script>
