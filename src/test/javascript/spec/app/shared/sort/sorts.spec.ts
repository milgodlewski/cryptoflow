import buildPaginationQueryOpts from '../../../......mainwebappapp/shared/sort/sorts';

describe('Sort', () => {
  it('should return an empty string if there is no pagination', () => {
    const result = buildPaginationQueryOpts(undefined);

    expect(result).toBe('');
  });
});
