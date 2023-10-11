/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import CurrencyPairMapping from '../../../......mainwebappapp/entities/currency-pair-mapping/currency-pair-mapping.vue';
import CurrencyPairMappingService from '../../../......mainwebappapp/entities/currency-pair-mapping/currency-pair-mapping.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type CurrencyPairMappingComponentType = InstanceType<typeof CurrencyPairMapping>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CurrencyPairMapping Management Component', () => {
    let currencyPairMappingServiceStub: SinonStubbedInstance<CurrencyPairMappingService>;
    let mountOptions: MountingOptions<CurrencyPairMappingComponentType>['global'];

    beforeEach(() => {
      currencyPairMappingServiceStub = sinon.createStubInstance<CurrencyPairMappingService>(CurrencyPairMappingService);
      currencyPairMappingServiceStub.retrieve.resolves({ headers: {} });

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
          currencyPairMappingService: () => currencyPairMappingServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        currencyPairMappingServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CurrencyPairMapping, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(currencyPairMappingServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.currencyPairMappings[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: CurrencyPairMappingComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CurrencyPairMapping, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        currencyPairMappingServiceStub.retrieve.reset();
        currencyPairMappingServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        currencyPairMappingServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCurrencyPairMapping();
        await comp.$nextTick(); // clear components

        // THEN
        expect(currencyPairMappingServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(currencyPairMappingServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
