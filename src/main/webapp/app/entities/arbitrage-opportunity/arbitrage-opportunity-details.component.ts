import { defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { IArbitrageOpportunity } from '@/shared/model/arbitrage-opportunity.model';
import ArbitrageOpportunityService from './arbitrage-opportunity.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ArbitrageOpportunityDetails',
  setup() {
    const arbitrageOpportunityService = inject('arbitrageOpportunityService', () => new ArbitrageOpportunityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const arbitrageOpportunity: Ref<IArbitrageOpportunity> = ref({});

    const retrieveArbitrageOpportunity = async arbitrageOpportunityId => {
      try {
        const res = await arbitrageOpportunityService().find(arbitrageOpportunityId);
        arbitrageOpportunity.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.arbitrageOpportunityId) {
      retrieveArbitrageOpportunity(route.params.arbitrageOpportunityId);
    }

    return {
      alertService,
      arbitrageOpportunity,

      previousState,
    };
  },
});
