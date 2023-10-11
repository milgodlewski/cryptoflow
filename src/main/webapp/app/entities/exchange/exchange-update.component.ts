import { computed, defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { IExchange, Exchange } from '@/shared/model/exchange.model';
import ExchangeService from './exchange.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExchangeUpdate',
  setup() {
    const exchangeService = inject('exchangeService', () => new ExchangeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const exchange: Ref<IExchange> = ref(new Exchange());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveExchange = async exchangeId => {
      try {
        const res = await exchangeService().find(exchangeId);
        exchange.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.exchangeId) {
      retrieveExchange(route.params.exchangeId);
    }

    const initRelationships = () => {};

    initRelationships();

    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required('This field is required.'),
      },
      currencyPairs: {},
    };
    const v$ = useVuelidate(validationRules, exchange as any);
    v$.value.$validate();

    return {
      exchangeService,
      alertService,
      exchange,
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
      if (this.exchange.id) {
        this.exchangeService()
          .update(this.exchange)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A Exchange is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.exchangeService()
          .create(this.exchange)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A Exchange is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
