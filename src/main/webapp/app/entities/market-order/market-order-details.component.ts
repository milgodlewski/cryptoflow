import { defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { IMarketOrder } from '@/shared/model/market-order.model';
import MarketOrderService from './market-order.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MarketOrderDetails',
  setup() {
    const marketOrderService = inject('marketOrderService', () => new MarketOrderService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const marketOrder: Ref<IMarketOrder> = ref({});

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

    return {
      alertService,
      marketOrder,

      previousState,
    };
  },
});
