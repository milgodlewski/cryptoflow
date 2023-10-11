import { IExchange } from '@/shared/model/exchange.model';
import { IMarketOrder } from '@/shared/model/market-order.model';

export interface ICurrencyPair {
  id?: number;
  baseCurrency?: string;
  targetCurrency?: string;
  exchangeRate?: number;
  exchanges?: IExchange[] | null;
  buyOrders?: IMarketOrder[] | null;
  sellOrders?: IMarketOrder[] | null;
}

export class CurrencyPair implements ICurrencyPair {
  constructor(
    public id?: number,
    public baseCurrency?: string,
    public targetCurrency?: string,
    public exchangeRate?: number,
    public exchanges?: IExchange[] | null,
    public buyOrders?: IMarketOrder[] | null,
    public sellOrders?: IMarketOrder[] | null
  ) {}
}
