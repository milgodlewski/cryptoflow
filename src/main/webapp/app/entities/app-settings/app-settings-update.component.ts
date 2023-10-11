import { computed, defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { IAppSettings, AppSettings } from '@/shared/model/app-settings.model';
import AppSettingsService from './app-settings.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppSettingsUpdate',
  setup() {
    const appSettingsService = inject('appSettingsService', () => new AppSettingsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const appSettings: Ref<IAppSettings> = ref(new AppSettings());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAppSettings = async appSettingsId => {
      try {
        const res = await appSettingsService().find(appSettingsId);
        appSettings.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.appSettingsId) {
      retrieveAppSettings(route.params.appSettingsId);
    }

    const validations = useValidation();
    const validationRules = {};
    const v$ = useVuelidate(validationRules, appSettings as any);
    v$.value.$validate();

    return {
      appSettingsService,
      alertService,
      appSettings,
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
      if (this.appSettings.id) {
        this.appSettingsService()
          .update(this.appSettings)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo('A AppSettings is updated with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.appSettingsService()
          .create(this.appSettings)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess('A AppSettings is created with identifier ' + param.id);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
