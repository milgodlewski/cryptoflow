import { computed, defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { ICurrencyPairMapping, CurrencyPairMapping } from '@/shared/model/currency-pair-mapping.model';
import CurrencyPairMappingService from './currency-pair-mapping.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'CurrencyPairMappingUpdate',
  setup() {
    const currencyPairMappingService = inject('currencyPairMappingService', () => new CurrencyPairMappingService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const currencyPairMapping: Ref<ICurrencyPairMapping> = ref(new CurrencyPairMapping());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const validations = useValidation();
    const validationRules = {
      baseCurrency: {
        required: validations.required('This field is required.'),
      },
      targetCurrency: {
        required: validations.required('This field is required.'),
      },
      sourceExchange: {
        required: validations.required('This field is required.'),
      },
      targetExchange: {
        required: validations.required('This field is required.'),
      },
    };
    const v$ = useVuelidate(validationRules, currencyPairMapping as any);
    v$.value.$validate();

    return {
      currencyPairMappingService,
      alertService,
      currencyPairMapping,
      previousState,
      isSaving,
      currentLanguage,
      v$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.currencyPairMapping.id) {
        this.currencyPairMappingService()
          .update(this.currencyPairMapping)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A CurrencyPairMapping is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.currencyPairMappingService()
          .create(this.currencyPairMapping)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A CurrencyPairMapping is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
