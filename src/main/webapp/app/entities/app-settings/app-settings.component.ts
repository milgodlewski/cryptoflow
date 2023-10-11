import { defineComponent, inject, onMounted, ref, Ref, watch, watchEffect } from 'vue';

import { IAppSettings } from '@/shared/model/app-settings.model';
import AppSettingsService from './app-settings.service';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppSettings',
  setup() {
    const appSettingsService = inject('appSettingsService', () => new AppSettingsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const appSettings: Ref<IAppSettings[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveAppSettingss = async () => {
      isFetching.value = true;
      try {
        const res = await appSettingsService().retrieve();
        appSettings.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveAppSettingss();
    };

    onMounted(async () => {
      await retrieveAppSettingss();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IAppSettings) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeAppSettings = async () => {
      try {
        await appSettingsService().delete(removeId.value);
        const message = 'A AppSettings is deleted with identifier ' + removeId.value;
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveAppSettingss();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      appSettings,
      handleSyncList,
      isFetching,
      retrieveAppSettingss,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeAppSettings,
    };
  },
});
