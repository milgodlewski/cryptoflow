import { defineComponent, inject, onMounted, ref, Ref, watch, watchEffect } from 'vue';

import { ICurrencyPair } from '@/shared/model/currency-pair.model';
import CurrencyPairService from './currency-pair.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CurrencyPair',
  setup() {
    const currencyPairService = inject('currencyPairService', () => new CurrencyPairService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const currencyPairs: Ref<ICurrencyPair[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveCurrencyPairs = async () => {
      isFetching.value = true;
      try {
        const res = await currencyPairService().retrieve();
        currencyPairs.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCurrencyPairs();
    };

    onMounted(async () => {
      await retrieveCurrencyPairs();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICurrencyPair) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCurrencyPair = async () => {
      try {
        await currencyPairService().delete(removeId.value);
        const message = 'A CurrencyPair is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCurrencyPairs();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      currencyPairs,
      handleSyncList,
      isFetching,
      retrieveCurrencyPairs,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCurrencyPair,
    };
  },
});
