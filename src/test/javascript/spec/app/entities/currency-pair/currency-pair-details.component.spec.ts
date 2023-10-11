/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import CurrencyPairDetails from '../../../......mainwebappapp/entities/currency-pair/currency-pair-details.vue';
import CurrencyPairService from '../../../......mainwebappapp/entities/currency-pair/currency-pair.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type CurrencyPairDetailsComponentType = InstanceType<typeof CurrencyPairDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const currencyPairSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CurrencyPair Management Detail Component', () => {
    let currencyPairServiceStub: SinonStubbedInstance<CurrencyPairService>;
    let mountOptions: MountingOptions<CurrencyPairDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      currencyPairServiceStub = sinon.createStubInstance<CurrencyPairService>(CurrencyPairService);

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
          currencyPairService: () => currencyPairServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        currencyPairServiceStub.find.resolves(currencyPairSample);
        route = {
          params: {
            currencyPairId: '' + 123,
          },
        };
        const wrapper = shallowMount(CurrencyPairDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.currencyPair).toMatchObject(currencyPairSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        currencyPairServiceStub.find.resolves(currencyPairSample);
        const wrapper = shallowMount(CurrencyPairDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
