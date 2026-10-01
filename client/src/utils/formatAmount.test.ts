import { describe, expect, it } from 'vitest'
import { formatAmount } from './formatAmount'

describe('formatAmount', () => {
  it.each([
    { amount: 0, currency: 'ARS', expectedNumber: '0,00', expectedCurrency: '$' },
    { amount: 1234.5, currency: 'ARS', expectedNumber: '1.234,50', expectedCurrency: '$' },
    { amount: 10, currency: 'USD', expectedNumber: '10,00', expectedCurrency: 'US$' },
  ])('formatea $amount como moneda $currency', ({ amount, currency, expectedNumber, expectedCurrency }) => {
    const result = formatAmount(amount, currency)

    expect(result).toContain(expectedNumber)
    expect(result).toContain(expectedCurrency)
  })
})
