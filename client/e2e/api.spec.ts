
import { test, expect, type APIRequestContext } from '@playwright/test'

const API = (process.env.API_BASE_URL || 'http://localhost:8080')
  .replace(/\/$/, '')

type Category = {
  id: string
  name: string
  color: string
  isDefault: boolean
}

type Transaction = {
  id: string
  title: string
  amount: number
  type: string
}

async function getToken(request: APIRequestContext): Promise<string> {
  const email = process.env.API_TEST_EMAIL
  const password = process.env.API_TEST_PASSWORD

  if (!email || !password) {
    throw new Error('Configurar API_TEST_EMAIL y API_TEST_PASSWORD')
  }

  const response = await request.post(`${API}/api/v1/auth/login`, {
    data: { email, password }
  })

  expect(response.status()).toBe(200)

  const body = await response.json()
  expect(body.token).toBeTruthy()

  return body.token
}

function auth(token: string) {
  return { Authorization: `Bearer ${token}` }
}

function uniqueName(prefix: string): string {
  return `${prefix}-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
}

async function createCategory(
  request: APIRequestContext,
  token: string,
  name: string
): Promise<Category> {
  const response = await request.post(`${API}/api/v1/categories`, {
    headers: auth(token),
    data: {
      name,
      color: '#FF5733',
      isDefault: false
    }
  })

  expect(response.status()).toBe(200)

  const category = await response.json()
  expect(category.id).toBeTruthy()

  return category
}

async function deleteCategory(
  request: APIRequestContext,
  token: string,
  id: string
) {
  const response = await request.delete(
    `${API}/api/v1/categories/${id}`,
    { headers: auth(token) }
  )

  expect(response.ok()).toBeTruthy()
}

test('crear una categoria, verificar persistencia y eliminarla', async ({ request }) => {
  const token = await getToken(request)
  const name = uniqueName('integracion-categoria')
  const category = await createCategory(request, token, name)

  try {
    const response = await request.get(`${API}/api/v1/categories`, {
      headers: auth(token)
    })

    expect(response.status()).toBe(200)

    const categories: Category[] = await response.json()
    expect(categories.some(c => c.id === category.id && c.name === name))
      .toBe(true)
  } finally {
    await deleteCategory(request, token, category.id)
  }

  const response = await request.get(`${API}/api/v1/categories`, {
    headers: auth(token)
  })

  expect(response.status()).toBe(200)

  const categories: Category[] = await response.json()
  expect(categories.some(c => c.id === category.id)).toBe(false)
})

test('crear un gasto, consultarlo y eliminarlo', async ({ request }) => {
  const token = await getToken(request)
  const category = await createCategory(
    request, token, uniqueName('categoria-gasto')
  )

  let transactionId: string | undefined

  try {
    const title = uniqueName('integracion-gasto')

    const create = await request.post(`${API}/api/v1/transactions`, {
      headers: auth(token),
      data: {
        title,
        amount: 1500,
        type: 'EXPENSE',
        description: 'Gasto de prueba de integracion',
        categoryId: category.id
      }
    })

    expect(create.status()).toBe(200)

    const transaction: Transaction = await create.json()
    transactionId = transaction.id

    expect(transaction.title).toBe(title)

    const get = await request.get(
      `${API}/api/v1/transactions/${transactionId}`,
      { headers: auth(token) }
    )

    expect(get.status()).toBe(200)

    const saved: Transaction = await get.json()
    expect(saved.title).toBe(title)
    expect(Number(saved.amount)).toBe(1500)
    expect(saved.type).toBe('EXPENSE')

  } finally {
    if (transactionId) {
      const deleted = await request.delete(
        `${API}/api/v1/transactions/${transactionId}`,
        { headers: auth(token) }
      )

      expect(deleted.ok()).toBeTruthy()

      const list = await request.get(`${API}/api/v1/transactions`, {
        headers: auth(token)
      })
      expect(list.status()).toBe(200)

      const transactions: Transaction[] = await list.json()
      expect(transactions.some(t => t.id === transactionId)).toBe(false)
    }

    await deleteCategory(request, token, category.id)
  }
})

test('modificar una categoria y verificar el cambio', async ({ request }) => {
  const token = await getToken(request)
  const category = await createCategory(
    request, token, uniqueName('categoria-original')
  )

  try {
    const newName = uniqueName('categoria-modificada')

    const update = await request.put(
      `${API}/api/v1/categories/${category.id}`,
      {
        headers: auth(token),
        data: {
          name: newName,
          color: '#3366FF',
          isDefault: false
        }
      }
    )

    expect(update.status()).toBe(200)

    const response = await request.get(
      `${API}/api/v1/categories/${category.id}`,
      { headers: auth(token) }
    )

    expect(response.status()).toBe(200)

    const updated: Category = await response.json()
    expect(updated.name).toBe(newName)
    expect(updated.color.toUpperCase()).toBe('#3366FF')

  } finally {
    await deleteCategory(request, token, category.id)
  }
})
