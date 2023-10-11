import { ICurrencyPair } from '@/shared/model/currency-pair.model';

import { OrderType } from '@/shared/model/enumerations/order-type.model';
export interface IMarketOrder {
  id?: number;
  type?: keyof typeof OrderType;
  price?: number;
  amount?: number;
  buyCurrencyPairs?: ICurrencyPair[] | null;
  sellCurrencyPairs?: ICurrencyPair[] | null;
}

export class MarketOrder implements IMarketOrder {
  constructor(
    public id?: number,
    public type?: keyof typeof OrderType,
    public price?: number,
    public amount?: number,
    public buyCurrencyPairs?: ICurrencyPair[] | null,
    public sellCurrencyPairs?: ICurrencyPair[] | null
  ) {}
}
