import { defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { ICurrencyPairMapping } from '@/shared/model/currency-pair-mapping.model';
import CurrencyPairMappingService from './currency-pair-mapping.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CurrencyPairMappingDetails',
  setup() {
    const currencyPairMappingService = inject('currencyPairMappingService', () => new CurrencyPairMappingService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const currencyPairMapping: Ref<ICurrencyPairMapping> = ref({});

    const retrieveCurrencyPairMapping = async currencyPairMappingId => {
      try {
        const res = await currencyPairMappingService().find(currencyPairMappingId);
        currencyPairMapping.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.currencyPairMappingId) {
      retrieveCurrencyPairMapping(route.params.currencyPairMappingId);
    }

    return {
      alertService,
      currencyPairMapping,

      previousState,
    };
  },
});
