import { defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { IExchange } from '@/shared/model/exchange.model';
import ExchangeService from './exchange.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExchangeDetails',
  setup() {
    const exchangeService = inject('exchangeService', () => new ExchangeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const exchange: Ref<IExchange> = ref({});

    const retrieveExchange = async exchangeId => {
      try {
        const res = await exchangeService().find(exchangeId);
        exchange.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.exchangeId) {
      retrieveExchange(route.params.exchangeId);
    }

    return {
      alertService,
      exchange,

      previousState,
    };
  },
});
