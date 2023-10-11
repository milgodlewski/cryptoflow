/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import CurrencyPair from '../../../......mainwebappapp/entities/currency-pair/currency-pair.vue';
import CurrencyPairService from '../../../......mainwebappapp/entities/currency-pair/currency-pair.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type CurrencyPairComponentType = InstanceType<typeof CurrencyPair>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('CurrencyPair Management Component', () => {
    let currencyPairServiceStub: SinonStubbedInstance<CurrencyPairService>;
    let mountOptions: MountingOptions<CurrencyPairComponentType>['global'];

    beforeEach(() => {
      currencyPairServiceStub = sinon.createStubInstance<CurrencyPairService>(CurrencyPairService);
      currencyPairServiceStub.retrieve.resolves({ headers: {} });

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
          currencyPairService: () => currencyPairServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        currencyPairServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(CurrencyPair, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(currencyPairServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.currencyPairs[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: CurrencyPairComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(CurrencyPair, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        currencyPairServiceStub.retrieve.reset();
        currencyPairServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        currencyPairServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeCurrencyPair();
        await comp.$nextTick(); // clear components

        // THEN
        expect(currencyPairServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(currencyPairServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
