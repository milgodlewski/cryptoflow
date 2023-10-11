/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import ExchangeDetails from '../../../......mainwebappapp/entities/exchange/exchange-details.vue';
import ExchangeService from '../../../......mainwebappapp/entities/exchange/exchange.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type ExchangeDetailsComponentType = InstanceType<typeof ExchangeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const exchangeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Exchange Management Detail Component', () => {
    let exchangeServiceStub: SinonStubbedInstance<ExchangeService>;
    let mountOptions: MountingOptions<ExchangeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      exchangeServiceStub = sinon.createStubInstance<ExchangeService>(ExchangeService);

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
          exchangeService: () => exchangeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        exchangeServiceStub.find.resolves(exchangeSample);
        route = {
          params: {
            exchangeId: '' + 123,
          },
        };
        const wrapper = shallowMount(ExchangeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.exchange).toMatchObject(exchangeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        exchangeServiceStub.find.resolves(exchangeSample);
        const wrapper = shallowMount(ExchangeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
