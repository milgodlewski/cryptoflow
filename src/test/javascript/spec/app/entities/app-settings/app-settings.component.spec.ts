/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import AppSettings from '../../../......mainwebappapp/entities/app-settings/app-settings.vue';
import AppSettingsService from '../../../......mainwebappapp/entities/app-settings/app-settings.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type AppSettingsComponentType = InstanceType<typeof AppSettings>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('AppSettings Management Component', () => {
    let appSettingsServiceStub: SinonStubbedInstance<AppSettingsService>;
    let mountOptions: MountingOptions<AppSettingsComponentType>['global'];

    beforeEach(() => {
      appSettingsServiceStub = sinon.createStubInstance<AppSettingsService>(AppSettingsService);
      appSettingsServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          appSettingsService: () => appSettingsServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        appSettingsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(AppSettings, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(appSettingsServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.appSettings[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AppSettingsComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(AppSettings, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        appSettingsServiceStub.retrieve.reset();
        appSettingsServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        appSettingsServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAppSettings();
        await comp.$nextTick(); // clear components

        // THEN
        expect(appSettingsServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(appSettingsServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
