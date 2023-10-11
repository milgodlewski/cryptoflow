import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const AppSettings = () => import('@/entities/app-settings/app-settings.vue');
const AppSettingsUpdate = () => import('@/entities/app-settings/app-settings-update.vue');
const AppSettingsDetails = () => import('@/entities/app-settings/app-settings-details.vue');

const ArbitrageOpportunity = () => import('@/entities/arbitrage-opportunity/arbitrage-opportunity.vue');
const ArbitrageOpportunityUpdate = () => import('@/entities/arbitrage-opportunity/arbitrage-opportunity-update.vue');
const ArbitrageOpportunityDetails = () => import('@/entities/arbitrage-opportunity/arbitrage-opportunity-details.vue');

const CurrencyPair = () => import('@/entities/currency-pair/currency-pair.vue');
const CurrencyPairUpdate = () => import('@/entities/currency-pair/currency-pair-update.vue');
const CurrencyPairDetails = () => import('@/entities/currency-pair/currency-pair-details.vue');

const CurrencyPairMapping = () => import('@/entities/currency-pair-mapping/currency-pair-mapping.vue');
const CurrencyPairMappingUpdate = () => import('@/entities/currency-pair-mapping/currency-pair-mapping-update.vue');
const CurrencyPairMappingDetails = () => import('@/entities/currency-pair-mapping/currency-pair-mapping-details.vue');

const Exchange = () => import('@/entities/exchange/exchange.vue');
const ExchangeUpdate = () => import('@/entities/exchange/exchange-update.vue');
const ExchangeDetails = () => import('@/entities/exchange/exchange-details.vue');

const MarketOrder = () => import('@/entities/market-order/market-order.vue');
const MarketOrderUpdate = () => import('@/entities/market-order/market-order-update.vue');
const MarketOrderDetails = () => import('@/entities/market-order/market-order-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'app-settings',
      name: 'AppSettings',
      component: AppSettings,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-settings/new',
      name: 'AppSettingsCreate',
      component: AppSettingsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-settings/:appSettingsId/edit',
      name: 'AppSettingsEdit',
      component: AppSettingsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-settings/:appSettingsId/view',
      name: 'AppSettingsView',
      component: AppSettingsDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arbitrage-opportunity',
      name: 'ArbitrageOpportunity',
      component: ArbitrageOpportunity,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arbitrage-opportunity/new',
      name: 'ArbitrageOpportunityCreate',
      component: ArbitrageOpportunityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arbitrage-opportunity/:arbitrageOpportunityId/edit',
      name: 'ArbitrageOpportunityEdit',
      component: ArbitrageOpportunityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arbitrage-opportunity/:arbitrageOpportunityId/view',
      name: 'ArbitrageOpportunityView',
      component: ArbitrageOpportunityDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'currency-pair',
      name: 'CurrencyPair',
      component: CurrencyPair,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'currency-pair/new',
      name: 'CurrencyPairCreate',
      component: CurrencyPairUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'currency-pair/:currencyPairId/edit',
      name: 'CurrencyPairEdit',
      component: CurrencyPairUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'currency-pair/:currencyPairId/view',
      name: 'CurrencyPairView',
      component: CurrencyPairDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'currency-pair-mapping',
      name: 'CurrencyPairMapping',
      component: CurrencyPairMapping,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'currency-pair-mapping/new',
      name: 'CurrencyPairMappingCreate',
      component: CurrencyPairMappingUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'currency-pair-mapping/:currencyPairMappingId/edit',
      name: 'CurrencyPairMappingEdit',
      component: CurrencyPairMappingUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'currency-pair-mapping/:currencyPairMappingId/view',
      name: 'CurrencyPairMappingView',
      component: CurrencyPairMappingDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'exchange',
      name: 'Exchange',
      component: Exchange,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'exchange/new',
      name: 'ExchangeCreate',
      component: ExchangeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'exchange/:exchangeId/edit',
      name: 'ExchangeEdit',
      component: ExchangeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'exchange/:exchangeId/view',
      name: 'ExchangeView',
      component: ExchangeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'market-order',
      name: 'MarketOrder',
      component: MarketOrder,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'market-order/new',
      name: 'MarketOrderCreate',
      component: MarketOrderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'market-order/:marketOrderId/edit',
      name: 'MarketOrderEdit',
      component: MarketOrderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'market-order/:marketOrderId/view',
      name: 'MarketOrderView',
      component: MarketOrderDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
