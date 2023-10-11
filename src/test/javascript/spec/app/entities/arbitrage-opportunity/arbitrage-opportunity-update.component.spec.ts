/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import ArbitrageOpportunityUpdate from '../../../......mainwebappapp/entities/arbitrage-opportunity/arbitrage-opportunity-update.vue';
import ArbitrageOpportunityService from '../../../......mainwebappapp/entities/arbitrage-opportunity/arbitrage-opportunity.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type ArbitrageOpportunityUpdateComponentType = InstanceType<typeof ArbitrageOpportunityUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const arbitrageOpportunitySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ArbitrageOpportunityUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ArbitrageOpportunity Management Update Component', () => {
    let comp: ArbitrageOpportunityUpdateComponentType;
    let arbitrageOpportunityServiceStub: SinonStubbedInstance<ArbitrageOpportunityService>;

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
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          arbitrageOpportunityService: () => arbitrageOpportunityServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ArbitrageOpportunityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.arbitrageOpportunity = arbitrageOpportunitySample;
        arbitrageOpportunityServiceStub.update.resolves(arbitrageOpportunitySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(arbitrageOpportunityServiceStub.update.calledWith(arbitrageOpportunitySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        arbitrageOpportunityServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ArbitrageOpportunityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.arbitrageOpportunity = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(arbitrageOpportunityServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        arbitrageOpportunityServiceStub.find.resolves(arbitrageOpportunitySample);
        arbitrageOpportunityServiceStub.retrieve.resolves([arbitrageOpportunitySample]);

        // WHEN
        route = {
          params: {
            arbitrageOpportunityId: '' + arbitrageOpportunitySample.id,
          },
        };
        const wrapper = shallowMount(ArbitrageOpportunityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.arbitrageOpportunity).toMatchObject(arbitrageOpportunitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        arbitrageOpportunityServiceStub.find.resolves(arbitrageOpportunitySample);
        const wrapper = shallowMount(ArbitrageOpportunityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
