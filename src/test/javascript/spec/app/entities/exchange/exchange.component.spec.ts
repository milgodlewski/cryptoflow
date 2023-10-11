/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, MountingOptions } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import Exchange from '../../../......mainwebappapp/entities/exchange/exchange.vue';
import ExchangeService from '../../../......mainwebappapp/entities/exchange/exchange.service';
import AlertService from '../../../......mainwebappapp/shared/alert/alert.service';

type ExchangeComponentType = InstanceType<typeof Exchange>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Exchange Management Component', () => {
    let exchangeServiceStub: SinonStubbedInstance<ExchangeService>;
    let mountOptions: MountingOptions<ExchangeComponentType>['global'];

    beforeEach(() => {
      exchangeServiceStub = sinon.createStubInstance<ExchangeService>(ExchangeService);
      exchangeServiceStub.retrieve.resolves({ headers: {} });

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
          exchangeService: () => exchangeServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        exchangeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(Exchange, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(exchangeServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.exchanges[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ExchangeComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Exchange, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        exchangeServiceStub.retrieve.reset();
        exchangeServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        exchangeServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeExchange();
        await comp.$nextTick(); // clear components

        // THEN
        expect(exchangeServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(exchangeServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
