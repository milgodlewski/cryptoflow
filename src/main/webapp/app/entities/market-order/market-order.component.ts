import { defineComponent, inject, onMounted, ref, Ref, watch, watchEffect } from 'vue';

import { IMarketOrder } from '@/shared/model/market-order.model';
import MarketOrderService from './market-order.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MarketOrder',
  setup() {
    const marketOrderService = inject('marketOrderService', () => new MarketOrderService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const marketOrders: Ref<IMarketOrder[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveMarketOrders = async () => {
      isFetching.value = true;
      try {
        const res = await marketOrderService().retrieve();
        marketOrders.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveMarketOrders();
    };

    onMounted(async () => {
      await retrieveMarketOrders();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IMarketOrder) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeMarketOrder = async () => {
      try {
        await marketOrderService().delete(removeId.value);
        const message = 'A MarketOrder is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveMarketOrders();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      marketOrders,
      handleSyncList,
      isFetching,
      retrieveMarketOrders,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeMarketOrder,
    };
  },
});
