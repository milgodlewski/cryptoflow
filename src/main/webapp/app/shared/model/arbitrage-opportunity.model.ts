export interface IArbitrageOpportunity {
  id?: number;
  baseCurrency?: string;
  targetCurrency?: string;
  sourceExchange?: string;
  targetExchange?: string;
  opportunityPercentage?: number | null;
}

export class ArbitrageOpportunity implements IArbitrageOpportunity {
  constructor(
    public id?: number,
    public baseCurrency?: string,
    public targetCurrency?: string,
    public sourceExchange?: string,
    public targetExchange?: string,
    public opportunityPercentage?: number | null
  ) {}
}
