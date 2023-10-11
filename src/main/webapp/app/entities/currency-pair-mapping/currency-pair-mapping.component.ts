import { defineComponent, inject, onMounted, ref, Ref, watch, watchEffect } from 'vue';

import { ICurrencyPairMapping } from '@/shared/model/currency-pair-mapping.model';
import CurrencyPairMappingService from './currency-pair-mapping.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CurrencyPairMapping',
  setup() {
    const currencyPairMappingService = inject('currencyPairMappingService', () => new CurrencyPairMappingService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const currencyPairMappings: Ref<ICurrencyPairMapping[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveCurrencyPairMappings = async () => {
      isFetching.value = true;
      try {
        const res = await currencyPairMappingService().retrieve();
        currencyPairMappings.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveCurrencyPairMappings();
    };

    onMounted(async () => {
      await retrieveCurrencyPairMappings();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ICurrencyPairMapping) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeCurrencyPairMapping = async () => {
      try {
        await currencyPairMappingService().delete(removeId.value);
        const message = 'A CurrencyPairMapping is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveCurrencyPairMappings();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      currencyPairMappings,
      handleSyncList,
      isFetching,
      retrieveCurrencyPairMappings,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeCurrencyPairMapping,
    };
  },
});
