/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import CurrencyPairMappingUpdate from '../../../......mainwebappapp/entities/currency-pair-mapping/currency-pair-mapping-update.vue';
import CurrencyPairMappingService from '../../../......mainwebappapp/entities/currency-pair-mapping/currency-pair-mapping.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type CurrencyPairMappingUpdateComponentType = InstanceType<typeof CurrencyPairMappingUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const currencyPairMappingSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CurrencyPairMappingUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CurrencyPairMapping Management Update Component', () => {
    let comp: CurrencyPairMappingUpdateComponentType;
    let currencyPairMappingServiceStub: SinonStubbedInstance<CurrencyPairMappingService>;

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
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          currencyPairMappingService: () => currencyPairMappingServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CurrencyPairMappingUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.currencyPairMapping = currencyPairMappingSample;
        currencyPairMappingServiceStub.update.resolves(currencyPairMappingSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(currencyPairMappingServiceStub.update.calledWith(currencyPairMappingSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        currencyPairMappingServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CurrencyPairMappingUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.currencyPairMapping = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(currencyPairMappingServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        currencyPairMappingServiceStub.find.resolves(currencyPairMappingSample);
        currencyPairMappingServiceStub.retrieve.resolves([currencyPairMappingSample]);

        // WHEN
        route = {
          params: {
            currencyPairMappingId: '' + currencyPairMappingSample.id,
          },
        };
        const wrapper = shallowMount(CurrencyPairMappingUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.currencyPairMapping).toMatchObject(currencyPairMappingSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        currencyPairMappingServiceStub.find.resolves(currencyPairMappingSample);
        const wrapper = shallowMount(CurrencyPairMappingUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
