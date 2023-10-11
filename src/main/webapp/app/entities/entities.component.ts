import { defineComponent, provide } from 'vue';

import UserService from '@/entities/user/user.service';
import AppSettingsService from './app-settings/app-settings.service';
import ArbitrageOpportunityService from './arbitrage-opportunity/arbitrage-opportunity.service';
import CurrencyPairService from './currency-pair/currency-pair.service';
import CurrencyPairMappingService from './currency-pair-mapping/currency-pair-mapping.service';
import ExchangeService from './exchange/exchange.service';
import MarketOrderService from './market-order/market-order.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('appSettingsService', () => new AppSettingsService());
    provide('arbitrageOpportunityService', () => new ArbitrageOpportunityService());
    provide('currencyPairService', () => new CurrencyPairService());
    provide('currencyPairMappingService', () => new CurrencyPairMappingService());
    provide('exchangeService', () => new ExchangeService());
    provide('marketOrderService', () => new MarketOrderService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
