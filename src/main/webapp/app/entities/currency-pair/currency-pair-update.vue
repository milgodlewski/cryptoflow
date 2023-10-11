<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="ncryptoflowApp.currencyPair.home.createOrEditLabel" data-cy="CurrencyPairCreateUpdateHeading">
          Create or edit a Currency Pair
        </h2>
        <div>
          <div class="form-group" v-if="currencyPair.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="currencyPair.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="currency-pair-baseCurrency">Base Currency</label>
            <input
              type="text"
              class="form-control"
              name="baseCurrency"
              id="currency-pair-baseCurrency"
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
            <label class="form-control-label" for="currency-pair-targetCurrency">Target Currency</label>
            <input
              type="text"
              class="form-control"
              name="targetCurrency"
              id="currency-pair-targetCurrency"
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
            <label class="form-control-label" for="currency-pair-exchangeRate">Exchange Rate</label>
            <input
              type="number"
              class="form-control"
              name="exchangeRate"
              id="currency-pair-exchangeRate"
              data-cy="exchangeRate"
              :class="{ valid: !v$.exchangeRate.$invalid, invalid: v$.exchangeRate.$invalid }"
              v-model.number="v$.exchangeRate.$model"
              required
            />
            <div v-if="v$.exchangeRate.$anyDirty && v$.exchangeRate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.exchangeRate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label for="currency-pair-exchanges">Exchanges</label>
            <select
              class="form-control"
              id="currency-pair-exchanges"
              data-cy="exchanges"
              multiple
              name="exchanges"
              v-if="currencyPair.exchanges !== undefined"
              v-model="currencyPair.exchanges"
            >
              <option
                v-bind:value="getSelected(currencyPair.exchanges, exchangeOption)"
                v-for="exchangeOption in exchanges"
                :key="exchangeOption.id"
              >
                {{ exchangeOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="currency-pair-buyOrders">Buy Orders</label>
            <select
              class="form-control"
              id="currency-pair-buyOrders"
              data-cy="buyOrders"
              multiple
              name="buyOrders"
              v-if="currencyPair.buyOrders !== undefined"
              v-model="currencyPair.buyOrders"
            >
              <option
                v-bind:value="getSelected(currencyPair.buyOrders, marketOrderOption)"
                v-for="marketOrderOption in marketOrders"
                :key="marketOrderOption.id"
              >
                {{ marketOrderOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="currency-pair-sellOrders">Sell Orders</label>
            <select
              class="form-control"
              id="currency-pair-sellOrders"
              data-cy="sellOrders"
              multiple
              name="sellOrders"
              v-if="currencyPair.sellOrders !== undefined"
              v-model="currencyPair.sellOrders"
            >
              <option
                v-bind:value="getSelected(currencyPair.sellOrders, marketOrderOption)"
                v-for="marketOrderOption in marketOrders"
                :key="marketOrderOption.id"
              >
                {{ marketOrderOption.id }}
              </option>
            </select>
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
<script lang="ts" src="./currency-pair-update.component.ts"></script>
