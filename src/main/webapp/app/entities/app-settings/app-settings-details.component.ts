import { defineComponent, inject, ref, Ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { IAppSettings } from '@/shared/model/app-settings.model';
import AppSettingsService from './app-settings.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppSettingsDetails',
  setup() {
    const appSettingsService = inject('appSettingsService', () => new AppSettingsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const appSettings: Ref<IAppSettings> = ref({});

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

    return {
      alertService,
      appSettings,

      previousState,
    };
  },
});
