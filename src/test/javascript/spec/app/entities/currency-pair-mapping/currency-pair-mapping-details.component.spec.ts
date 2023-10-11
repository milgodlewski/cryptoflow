/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import CurrencyPairMappingDetails from '../../../......mainwebappapp/entities/currency-pair-mapping/currency-pair-mapping-details.vue';
import CurrencyPairMappingService from '../../../......mainwebappapp/entities/currency-pair-mapping/currency-pair-mapping.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type CurrencyPairMappingDetailsComponentType = InstanceType<typeof CurrencyPairMappingDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const currencyPairMappingSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('CurrencyPairMapping Management Detail Component', () => {
    let currencyPairMappingServiceStub: SinonStubbedInstance<CurrencyPairMappingService>;
    let mountOptions: MountingOptions<CurrencyPairMappingDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      currencyPairMappingServiceStub = sinon.createStubInstance<CurrencyPairMappingService>(CurrencyPairMappingService);

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
          currencyPairMappingService: () => currencyPairMappingServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        currencyPairMappingServiceStub.find.resolves(currencyPairMappingSample);
        route = {
          params: {
            currencyPairMappingId: '' + 123,
          },
        };
        const wrapper = shallowMount(CurrencyPairMappingDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.currencyPairMapping).toMatchObject(currencyPairMappingSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        currencyPairMappingServiceStub.find.resolves(currencyPairMappingSample);
        const wrapper = shallowMount(CurrencyPairMappingDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
