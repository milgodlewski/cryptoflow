import { computed, defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { IMarketOrder, MarketOrder } from '@/shared/model/market-order.model';
import MarketOrderService from './market-order.service';
import { OrderType } from '@/shared/model/enumerations/order-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MarketOrderUpdate',
  setup() {
    const marketOrderService = inject('marketOrderService', () => new MarketOrderService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const marketOrder: Ref<IMarketOrder> = ref(new MarketOrder());
    const orderTypeValues: Ref<string[]> = ref(Object.keys(OrderType));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMarketOrder = async marketOrderId => {
      try {
        const res = await marketOrderService().find(marketOrderId);
        marketOrder.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.marketOrderId) {
      retrieveMarketOrder(route.params.marketOrderId);
    }

    const initRelationships = () => {};

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      type: {
        required: validations.required('This field is required.'),
      },
      price: {
        required: validations.required('This field is required.'),
      },
      amount: {
        required: validations.required('This field is required.'),
      },
      buyCurrencyPairs: {},
      sellCurrencyPairs: {},
    };
    const v$ = useVuelidate(validationRules, marketOrder as any);
    v$.value.$validate();

    return {
      marketOrderService,
      alertService,
      marketOrder,
      previousState,
      orderTypeValues,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.marketOrder.id) {
        this.marketOrderService()
          .update(this.marketOrder)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A MarketOrder is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.marketOrderService()
          .create(this.marketOrder)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A MarketOrder is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
