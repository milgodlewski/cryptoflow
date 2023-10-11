/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import MarketOrder from '../../../......mainwebappapp/entities/market-order/market-order.vue';
import MarketOrderService from '../../../......mainwebappapp/entities/market-order/market-order.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type MarketOrderComponentType = InstanceType<typeof MarketOrder>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('MarketOrder Management Component', () => {
    let marketOrderServiceStub: SinonStubbedInstance<MarketOrderService>;
    let mountOptions: MountingOptions<MarketOrderComponentType>['global'];

    beforeEach(() => {
      marketOrderServiceStub = sinon.createStubInstance<MarketOrderService>(MarketOrderService);
      marketOrderServiceStub.retrieve.resolves({ headers: {} });

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
          marketOrderService: () => marketOrderServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        marketOrderServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(MarketOrder, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(marketOrderServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.marketOrders[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: MarketOrderComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(MarketOrder, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        marketOrderServiceStub.retrieve.reset();
        marketOrderServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        marketOrderServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeMarketOrder();
        await comp.$nextTick(); // clear components

        // THEN
        expect(marketOrderServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(marketOrderServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
