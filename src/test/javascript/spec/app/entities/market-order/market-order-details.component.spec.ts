/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import MarketOrderDetails from '../../../......mainwebappapp/entities/market-order/market-order-details.vue';
import MarketOrderService from '../../../......mainwebappapp/entities/market-order/market-order.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type MarketOrderDetailsComponentType = InstanceType<typeof MarketOrderDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const marketOrderSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MarketOrder Management Detail Component', () => {
    let marketOrderServiceStub: SinonStubbedInstance<MarketOrderService>;
    let mountOptions: MountingOptions<MarketOrderDetailsComponentType>['global'];

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
          'router-link': true,
        },
        provide: {
          alertService,
          marketOrderService: () => marketOrderServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        marketOrderServiceStub.find.resolves(marketOrderSample);
        route = {
          params: {
            marketOrderId: '' + 123,
          },
        };
        const wrapper = shallowMount(MarketOrderDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.marketOrder).toMatchObject(marketOrderSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        marketOrderServiceStub.find.resolves(marketOrderSample);
        const wrapper = shallowMount(MarketOrderDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
