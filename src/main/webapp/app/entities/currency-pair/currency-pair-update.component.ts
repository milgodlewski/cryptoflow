import { computed, defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ExchangeService from '@/entities/exchange/exchange.service';
import { IExchange } from '@/shared/model/exchange.model';
import MarketOrderService from '@/entities/market-order/market-order.service';
import { IMarketOrder } from '@/shared/model/market-order.model';
import { ICurrencyPair, CurrencyPair } from '@/shared/model/currency-pair.model';
import CurrencyPairService from './currency-pair.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CurrencyPairUpdate',
  setup() {
    const currencyPairService = inject('currencyPairService', () => new CurrencyPairService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const currencyPair: Ref<ICurrencyPair> = ref(new CurrencyPair());
    const exchangeService = inject('exchangeService', () => new ExchangeService());
    const exchanges: Ref<IExchange[]> = ref([]);
    const marketOrderService = inject('marketOrderService', () => new MarketOrderService());
    const marketOrders: Ref<IMarketOrder[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveCurrencyPair = async currencyPairId => {
      try {
        const res = await currencyPairService().find(currencyPairId);
        currencyPair.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.currencyPairId) {
      retrieveCurrencyPair(route.params.currencyPairId);
    }

    const initRelationships = () => {
      exchangeService()
        .retrieve()
        .then(res => {
          exchanges.value = res.data;
        });
      marketOrderService()
        .retrieve()
        .then(res => {
          marketOrders.value = res.data;
        });
    };

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      baseCurrency: {
        required: validations.required('This field is required.'),
      },
      targetCurrency: {
        required: validations.required('This field is required.'),
      },
      exchangeRate: {
        required: validations.required('This field is required.'),
      },
      exchanges: {},
      buyOrders: {},
      sellOrders: {},
    };
    const v$ = useVuelidate(validationRules, currencyPair as any);
    v$.value.$validate();

    return {
      currencyPairService,
      alertService,
      currencyPair,
      previousState,
      isSaving,
      currentLanguage,
      exchanges,
      marketOrders,
      v$,
    };
  },
  created(): void {
    this.currencyPair.exchanges = [];
    this.currencyPair.buyOrders = [];
    this.currencyPair.sellOrders = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.currencyPair.id) {
        this.currencyPairService()
          .update(this.currencyPair)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A CurrencyPair is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.currencyPairService()
          .create(this.currencyPair)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A CurrencyPair is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option): any {
      if (selectedVals) {
        return selectedVals.find(value => option.id === value.id) ?? option;
      }
      return option;
    },
  },
});
