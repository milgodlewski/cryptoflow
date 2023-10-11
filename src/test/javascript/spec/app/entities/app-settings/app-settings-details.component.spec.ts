/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import AppSettingsDetails from '../../../......mainwebappapp/entities/app-settings/app-settings-details.vue';
import AppSettingsService from '../../../......mainwebappapp/entities/app-settings/app-settings.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type AppSettingsDetailsComponentType = InstanceType<typeof AppSettingsDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const appSettingsSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('AppSettings Management Detail Component', () => {
    let appSettingsServiceStub: SinonStubbedInstance<AppSettingsService>;
    let mountOptions: MountingOptions<AppSettingsDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      appSettingsServiceStub = sinon.createStubInstance<AppSettingsService>(AppSettingsService);

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          appSettingsService: () => appSettingsServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        appSettingsServiceStub.find.resolves(appSettingsSample);
        route = {
          params: {
            appSettingsId: '' + 123,
          },
        };
        const wrapper = shallowMount(AppSettingsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.appSettings).toMatchObject(appSettingsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        appSettingsServiceStub.find.resolves(appSettingsSample);
        const wrapper = shallowMount(AppSettingsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
