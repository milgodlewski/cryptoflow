import { defineComponent, inject, onMounted, ref, Ref, watch, watchEffect } from 'vue';

import { IArbitrageOpportunity } from '@/shared/model/arbitrage-opportunity.model';
import ArbitrageOpportunityService from './arbitrage-opportunity.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ArbitrageOpportunity',
  setup() {
    const arbitrageOpportunityService = inject('arbitrageOpportunityService', () => new ArbitrageOpportunityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const arbitrageOpportunities: Ref<IArbitrageOpportunity[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveArbitrageOpportunitys = async () => {
      isFetching.value = true;
      try {
        const res = await arbitrageOpportunityService().retrieve();
        arbitrageOpportunities.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveArbitrageOpportunitys();
    };

    onMounted(async () => {
      await retrieveArbitrageOpportunitys();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IArbitrageOpportunity) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeArbitrageOpportunity = async () => {
      try {
        await arbitrageOpportunityService().delete(removeId.value);
        const message = 'A ArbitrageOpportunity is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveArbitrageOpportunitys();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      arbitrageOpportunities,
      handleSyncList,
      isFetching,
      retrieveArbitrageOpportunitys,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeArbitrageOpportunity,
    };
  },
});
