import { defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { ICurrencyPair } from '@/shared/model/currency-pair.model';
import CurrencyPairService from './currency-pair.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CurrencyPairDetails',
  setup() {
    const currencyPairService = inject('currencyPairService', () => new CurrencyPairService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const currencyPair: Ref<ICurrencyPair> = ref({});

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

    return {
      alertService,
      currencyPair,

      previousState,
    };
  },
});
