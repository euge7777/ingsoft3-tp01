import { describe, expect, it } from 'vitest'
import { formatDate } from './formatDate'

describe('formatDate', () => {
  it('genera los formatos necesarios para filtrar por día y mes', () => {
    const date = new Date(2026, 8, 30, 17, 5, 9)

    const result = formatDate(date)

    expect(result.currentMonth).toBe('2026-09')
    expect(result.currentDayAndTime).toBe('2026-09-30T17:05')
    expect(result.currentStartOfDay).toBe('2026-09-30T00:00:00')
    expect(result.currentEndOfDay).toBe('2026-09-30T23:59:59')
    expect(result.firstDayOfMonth).toBe('2026-09-01T00:00:00')
    expect(result.isoDateTime).toBe('2026-09-30T17:05:09')
  })

  it('suma y resta días sin modificar la fecha original', () => {
    const date = new Date(2026, 8, 30, 17, 5, 9)

    const result = formatDate(date)

    const nextDay = result.addDays(date, 1)
    const previousDays = result.subDays(date, 2)

    expect(formatDate(nextDay).currentStartOfDay).toBe('2026-10-01T00:00:00')
    expect(formatDate(previousDays).currentStartOfDay).toBe('2026-09-28T00:00:00')
    expect(formatDate(date).currentStartOfDay).toBe('2026-09-30T00:00:00')
  })
})
