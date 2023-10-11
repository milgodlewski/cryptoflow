import { defineComponent, inject, onMounted, ref, Ref, watch, watchEffect } from 'vue';

import { IExchange } from '@/shared/model/exchange.model';
import ExchangeService from './exchange.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Exchange',
  setup() {
    const exchangeService = inject('exchangeService', () => new ExchangeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const exchanges: Ref<IExchange[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveExchanges = async () => {
      isFetching.value = true;
      try {
        const res = await exchangeService().retrieve();
        exchanges.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveExchanges();
    };

    onMounted(async () => {
      await retrieveExchanges();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IExchange) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeExchange = async () => {
      try {
        await exchangeService().delete(removeId.value);
        const message = 'A Exchange is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveExchanges();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      exchanges,
      handleSyncList,
      isFetching,
      retrieveExchanges,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeExchange,
    };
  },
});
