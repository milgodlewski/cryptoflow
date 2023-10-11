import { ICurrencyPair } from '@/shared/model/currency-pair.model';

export interface IExchange {
  id?: number;
  name?: string;
  currencyPairs?: ICurrencyPair[] | null;
}

export class Exchange implements IExchange {
  constructor(public id?: number, public name?: string, public currencyPairs?: ICurrencyPair[] | null) {}
}
