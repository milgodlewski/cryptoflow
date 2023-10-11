/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import AppSettingsUpdate from '../../../......mainwebappapp/entities/app-settings/app-settings-update.vue';
import AppSettingsService from '../../../......mainwebappapp/entities/app-settings/app-settings.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type AppSettingsUpdateComponentType = InstanceType<typeof AppSettingsUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const appSettingsSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AppSettingsUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AppSettings Management Update Component', () => {
    let comp: AppSettingsUpdateComponentType;
    let appSettingsServiceStub: SinonStubbedInstance<AppSettingsService>;

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
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          appSettingsService: () => appSettingsServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AppSettingsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.appSettings = appSettingsSample;
        appSettingsServiceStub.update.resolves(appSettingsSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appSettingsServiceStub.update.calledWith(appSettingsSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        appSettingsServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AppSettingsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.appSettings = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appSettingsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        appSettingsServiceStub.find.resolves(appSettingsSample);
        appSettingsServiceStub.retrieve.resolves([appSettingsSample]);

        // WHEN
        route = {
          params: {
            appSettingsId: '' + appSettingsSample.id,
          },
        };
        const wrapper = shallowMount(AppSettingsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.appSettings).toMatchObject(appSettingsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        appSettingsServiceStub.find.resolves(appSettingsSample);
        const wrapper = shallowMount(AppSettingsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
