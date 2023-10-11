/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { RouteLocation } from 'vue-router';

import ExchangeUpdate from '../../../......mainwebappapp/entities/exchange/exchange-update.vue';
import ExchangeService from '../../../......mainwebappapp/entities/exchange/exchange.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type ExchangeUpdateComponentType = InstanceType<typeof ExchangeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const exchangeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ExchangeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Exchange Management Update Component', () => {
    let comp: ExchangeUpdateComponentType;
    let exchangeServiceStub: SinonStubbedInstance<ExchangeService>;

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
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          exchangeService: () => exchangeServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ExchangeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.exchange = exchangeSample;
        exchangeServiceStub.update.resolves(exchangeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(exchangeServiceStub.update.calledWith(exchangeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        exchangeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ExchangeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.exchange = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(exchangeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        exchangeServiceStub.find.resolves(exchangeSample);
        exchangeServiceStub.retrieve.resolves([exchangeSample]);

        // WHEN
        route = {
          params: {
            exchangeId: '' + exchangeSample.id,
          },
        };
        const wrapper = shallowMount(ExchangeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.exchange).toMatchObject(exchangeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        exchangeServiceStub.find.resolves(exchangeSample);
        const wrapper = shallowMount(ExchangeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
