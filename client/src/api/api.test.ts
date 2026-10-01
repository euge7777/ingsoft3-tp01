import { beforeEach, describe, expect, it, vi } from 'vitest'

const testState = vi.hoisted(() => ({
  token: null as string | null,
  logout: vi.fn(),
  requestUse: vi.fn(),
  responseUse: vi.fn(),
}))

vi.mock('axios', () => ({
  default: {
    create: vi.fn(() => ({
      interceptors: {
        request: {
          use: testState.requestUse,
        },
        response: {
          use: testState.responseUse,
        },
      },
    })),
  },
}))

vi.mock('../stores/useAuthStore', () => ({
  useAuthStore: {
    getState: () => ({
      token: testState.token,
      logout: testState.logout,
    }),
  },
}))

describe('api client', () => {
  beforeEach(async () => {
    vi.resetModules()

    testState.token = null
    testState.logout.mockClear()
    testState.requestUse.mockClear()
    testState.responseUse.mockClear()

    await import('./api')
  })

  it('agrega Authorization cuando existe token de sesión', () => {
    testState.token = 'token-test'

    const requestHandler = testState.requestUse.mock.calls[0][0]
    const config = requestHandler({ headers: {} })

    expect(config.headers.Authorization).toBe('Bearer token-test')
  })

  it('no agrega Authorization cuando no existe token de sesión', () => {
    const requestHandler = testState.requestUse.mock.calls[0][0]
    const config = requestHandler({ headers: {} })

    expect(config.headers).not.toHaveProperty('Authorization')
  })

  it.each([401, 403])('cierra sesión cuando la API responde %s', async (status) => {
    const errorHandler = testState.responseUse.mock.calls[0][1]
    const error = { response: { status } }

    await expect(errorHandler(error)).rejects.toBe(error)
    expect(testState.logout).toHaveBeenCalledTimes(1)
  })

  it('no cierra sesión ante un error que no es de autenticación', async () => {
    const errorHandler = testState.responseUse.mock.calls[0][1]
    const error = { response: { status: 500 } }

    await expect(errorHandler(error)).rejects.toBe(error)
    expect(testState.logout).not.toHaveBeenCalled()
  })
})
