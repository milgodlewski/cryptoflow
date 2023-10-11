/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import CurrencyPairUpdate from '../../../......mainwebappapp/entities/currency-pair/currency-pair-update.vue';
import CurrencyPairService from '../../../......mainwebappapp/entities/currency-pair/currency-pair.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

import ExchangeService from '../../../......mainwebappapp/entities/exchange/exchange.service';
import MarketOrderService from '../../../......mainwebappapp/entities/market-order/market-order.service';

type CurrencyPairUpdateComponentType = InstanceType<typeof CurrencyPairUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const currencyPairSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<CurrencyPairUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('CurrencyPair Management Update Component', () => {
    let comp: CurrencyPairUpdateComponentType;
    let currencyPairServiceStub: SinonStubbedInstance<CurrencyPairService>;

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
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          currencyPairService: () => currencyPairServiceStub,
          exchangeService: () =>
            sinon.createStubInstance<ExchangeService>(ExchangeService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          marketOrderService: () =>
            sinon.createStubInstance<MarketOrderService>(MarketOrderService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(CurrencyPairUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.currencyPair = currencyPairSample;
        currencyPairServiceStub.update.resolves(currencyPairSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(currencyPairServiceStub.update.calledWith(currencyPairSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        currencyPairServiceStub.create.resolves(entity);
        const wrapper = shallowMount(CurrencyPairUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.currencyPair = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(currencyPairServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        currencyPairServiceStub.find.resolves(currencyPairSample);
        currencyPairServiceStub.retrieve.resolves([currencyPairSample]);

        // WHEN
        route = {
          params: {
            currencyPairId: '' + currencyPairSample.id,
          },
        };
        const wrapper = shallowMount(CurrencyPairUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.currencyPair).toMatchObject(currencyPairSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        currencyPairServiceStub.find.resolves(currencyPairSample);
        const wrapper = shallowMount(CurrencyPairUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
