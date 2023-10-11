/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import ArbitrageOpportunity from '../../../......mainwebappapp/entities/arbitrage-opportunity/arbitrage-opportunity.vue';
import ArbitrageOpportunityService from '../../../......mainwebappapp/entities/arbitrage-opportunity/arbitrage-opportunity.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type ArbitrageOpportunityComponentType = InstanceType<typeof ArbitrageOpportunity>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ArbitrageOpportunity Management Component', () => {
    let arbitrageOpportunityServiceStub: SinonStubbedInstance<ArbitrageOpportunityService>;
    let mountOptions: MountingOptions<ArbitrageOpportunityComponentType>['global'];

    beforeEach(() => {
      arbitrageOpportunityServiceStub = sinon.createStubInstance<ArbitrageOpportunityService>(ArbitrageOpportunityService);
      arbitrageOpportunityServiceStub.retrieve.resolves({ headers: {} });

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
          arbitrageOpportunityService: () => arbitrageOpportunityServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        arbitrageOpportunityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ArbitrageOpportunity, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(arbitrageOpportunityServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.arbitrageOpportunities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ArbitrageOpportunityComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ArbitrageOpportunity, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        arbitrageOpportunityServiceStub.retrieve.reset();
        arbitrageOpportunityServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        arbitrageOpportunityServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeArbitrageOpportunity();
        await comp.$nextTick(); // clear components

        // THEN
        expect(arbitrageOpportunityServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(arbitrageOpportunityServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
