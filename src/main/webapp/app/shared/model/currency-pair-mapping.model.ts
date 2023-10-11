export interface ICurrencyPairMapping {
  id?: number;
  baseCurrency?: string;
  targetCurrency?: string;
  sourceExchange?: string;
  targetExchange?: string;
}

export class CurrencyPairMapping implements ICurrencyPairMapping {
  constructor(
    public id?: number,
    public baseCurrency?: string,
    public targetCurrency?: string,
    public sourceExchange?: string,
    public targetExchange?: string
  ) {}
}
