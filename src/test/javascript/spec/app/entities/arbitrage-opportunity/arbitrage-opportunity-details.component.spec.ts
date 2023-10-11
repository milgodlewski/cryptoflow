/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import ArbitrageOpportunityDetails from '../../../......mainwebappapp/entities/arbitrage-opportunity/arbitrage-opportunity-details.vue';
import ArbitrageOpportunityService from '../../../......mainwebappapp/entities/arbitrage-opportunity/arbitrage-opportunity.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type ArbitrageOpportunityDetailsComponentType = InstanceType<typeof ArbitrageOpportunityDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const arbitrageOpportunitySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ArbitrageOpportunity Management Detail Component', () => {
    let arbitrageOpportunityServiceStub: SinonStubbedInstance<ArbitrageOpportunityService>;
    let mountOptions: MountingOptions<ArbitrageOpportunityDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      arbitrageOpportunityServiceStub = sinon.createStubInstance<ArbitrageOpportunityService>(ArbitrageOpportunityService);

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
          arbitrageOpportunityService: () => arbitrageOpportunityServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        arbitrageOpportunityServiceStub.find.resolves(arbitrageOpportunitySample);
        route = {
          params: {
            arbitrageOpportunityId: '' + 123,
          },
        };
        const wrapper = shallowMount(ArbitrageOpportunityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.arbitrageOpportunity).toMatchObject(arbitrageOpportunitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        arbitrageOpportunityServiceStub.find.resolves(arbitrageOpportunitySample);
        const wrapper = shallowMount(ArbitrageOpportunityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
