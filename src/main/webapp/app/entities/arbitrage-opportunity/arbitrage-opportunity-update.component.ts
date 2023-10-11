import { computed, defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { IArbitrageOpportunity, ArbitrageOpportunity } from '@/shared/model/arbitrage-opportunity.model';
import ArbitrageOpportunityService from './arbitrage-opportunity.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ArbitrageOpportunityUpdate',
  setup() {
    const arbitrageOpportunityService = inject('arbitrageOpportunityService', () => new ArbitrageOpportunityService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const arbitrageOpportunity: Ref<IArbitrageOpportunity> = ref(new ArbitrageOpportunity());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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
      opportunityPercentage: {},
    };
    const v$ = useVuelidate(validationRules, arbitrageOpportunity as any);
    v$.value.$validate();

    return {
      arbitrageOpportunityService,
      alertService,
      arbitrageOpportunity,
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
      if (this.arbitrageOpportunity.id) {
        this.arbitrageOpportunityService()
          .update(this.arbitrageOpportunity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A ArbitrageOpportunity is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.arbitrageOpportunityService()
          .create(this.arbitrageOpportunity)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A ArbitrageOpportunity is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
