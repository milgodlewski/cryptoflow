/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import MarketOrderUpdate from '../../../......mainwebappapp/entities/market-order/market-order-update.vue';
import MarketOrderService from '../../../......mainwebappapp/entities/market-order/market-order.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type MarketOrderUpdateComponentType = InstanceType<typeof MarketOrderUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const marketOrderSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MarketOrderUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MarketOrder Management Update Component', () => {
    let comp: MarketOrderUpdateComponentType;
    let marketOrderServiceStub: SinonStubbedInstance<MarketOrderService>;

    beforeEach(() => {
      route = {};
      marketOrderServiceStub = sinon.createStubInstance<MarketOrderService>(MarketOrderService);

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
          marketOrderService: () => marketOrderServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(MarketOrderUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.marketOrder = marketOrderSample;
        marketOrderServiceStub.update.resolves(marketOrderSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(marketOrderServiceStub.update.calledWith(marketOrderSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        marketOrderServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MarketOrderUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.marketOrder = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(marketOrderServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        marketOrderServiceStub.find.resolves(marketOrderSample);
        marketOrderServiceStub.retrieve.resolves([marketOrderSample]);

        // WHEN
        route = {
          params: {
            marketOrderId: '' + marketOrderSample.id,
          },
        };
        const wrapper = shallowMount(MarketOrderUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.marketOrder).toMatchObject(marketOrderSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        marketOrderServiceStub.find.resolves(marketOrderSample);
        const wrapper = shallowMount(MarketOrderUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
