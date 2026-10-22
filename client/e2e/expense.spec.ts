
import {
  test,
  expect,
  type Page,
  type APIRequestContext
} from '@playwright/test'

const API = (
  process.env.API_BASE_URL || 'http://localhost:8080'
).replace(/\/$/, '')

const email = process.env.API_TEST_EMAIL
const password = process.env.API_TEST_PASSWORD

type Category = {
  id: string
  name: string
}

type Transaction = {
  id: string
  title: string
  amount: number
}

function unique(prefix: string) {
  return `${prefix}-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
}

function headers(jwt: string) {
  return {
    Authorization: `Bearer ${jwt}`
  }
}

async function token(request: APIRequestContext): Promise<string> {
  if (!email || !password) {
    throw new Error(
      'Faltan las variables API_TEST_EMAIL y API_TEST_PASSWORD'
    )
  }

  const response = await request.post(`${API}/api/v1/auth/login`, {
    data: { email, password },
    timeout: 20_000
  })

  expect(response.status()).toBe(200)

  const body = await response.json()
  expect(body.token).toBeTruthy()

  return body.token as string
}

async function login(page: Page) {
  if (!email || !password) {
    throw new Error('Faltan las credenciales de QA')
  }

  await page.goto('/login')

  await page.getByRole('textbox', {
    name: 'Enter your email'
  }).fill(email)

  await page.getByRole('textbox', {
    name: 'Enter your password'
  }).fill(password)

  await page.getByRole('button', {
    name: 'Login'
  }).click()

  await expect(
    page.getByRole('button', { name: 'Add Transaction' })
  ).toBeVisible()
}

async function categories(
  request: APIRequestContext,
  jwt: string
): Promise<Category[]> {
  const response = await request.get(`${API}/api/v1/categories`, {
    headers: headers(jwt),
    timeout: 15_000
  })

  expect(response.status()).toBe(200)
  return await response.json()
}

async function transactions(
  request: APIRequestContext,
  jwt: string
): Promise<Transaction[]> {
  const response = await request.get(`${API}/api/v1/transactions`, {
    headers: headers(jwt),
    timeout: 15_000
  })

  expect(response.status()).toBe(200)
  return await response.json()
}

async function createCategory(
  request: APIRequestContext,
  jwt: string
): Promise<Category> {
  const response = await request.post(`${API}/api/v1/categories`, {
    headers: headers(jwt),
    data: {
      name: unique('e2e-category'),
      color: '#F7C6C7',
      isDefault: false
    },
    timeout: 15_000
  })

  expect(response.status()).toBe(200)

  const category: Category = await response.json()
  expect(category.id).toBeTruthy()

  return category
}

async function removeCategory(
  request: APIRequestContext,
  jwt: string,
  id: string
) {
  const response = await request.delete(
    `${API}/api/v1/categories/${id}`,
    {
      headers: headers(jwt),
      timeout: 15_000
    }
  )

  expect(response.ok()).toBeTruthy()
}

async function removeTransaction(
  request: APIRequestContext,
  jwt: string,
  id: string
) {
  const response = await request.delete(
    `${API}/api/v1/transactions/${id}`,
    {
      headers: headers(jwt),
      timeout: 15_000
    }
  )

  expect(response.ok()).toBeTruthy()
}

async function createExpenseUI(
  page: Page,
  title: string,
  amount: string,
  categoryId: string
) {
  await page.getByRole('button', {
    name: 'Add Transaction'
  }).click()

  await page.getByRole('textbox', {
    name: 'Enter a title'
  }).fill(title)

  await page.getByRole('spinbutton', {
    name: 'Enter the transaction amount'
  }).fill(amount)

  await page.locator('select[name="type"]')
    .selectOption('EXPENSE')

  await page.getByRole('textbox', {
    name: 'Enter the transaction'
  }).fill('Gasto creado por Playwright E2E')

  await page.locator('select[name="categoryId"]')
    .selectOption(categoryId)

  await page.getByRole('button', {
    name: 'Create transaction'
  }).click()

  await expect(
    page.getByText(title, { exact: true })
  ).toBeVisible()
}

test('crear un ingreso con una categoria nueva', async ({ page, request }) => {
  test.setTimeout(90_000)

  const jwt = await token(request)
  const categoryName = unique('e2e-ingreso-categoria')
  const title = unique('e2e-ingreso')
  let categoryId: string | undefined

  try {
    await login(page)

    // 1. Abrir el formulario de transacciones
    await page.getByRole('button', {
      name: 'Add Transaction'
    }).click()

    // 2. Crear una categoría desde la interfaz
    await page.getByRole('button', {
      name: 'Add Category'
    }).click()

    await page.getByRole('textbox', {
      name: 'Enter your category name'
    }).fill(categoryName)

    await page.locator('select[name="color"]')
      .selectOption('#F7C6C7')

    await page.getByRole('button', {
      name: 'Create category'
    }).click()

    // 3. Verificar que se guardó en PostgreSQL
    await expect.poll(async () => {
      const list = await categories(request, jwt)
      const category = list.find(c => c.name === categoryName)
      categoryId = category?.id
      return Boolean(category)
    }, { timeout: 15_000 }).toBe(true)

    expect(categoryId).toBeTruthy()

    // 4. Abrir el formulario si se cerró
    const titleInput = page.getByRole('textbox', {
      name: 'Enter a title'
    })

    if (!(await titleInput.isVisible())) {
      await page.getByRole('button', {
        name: 'Add Transaction'
      }).click()
    }

    // 5. Registrar un ingreso
    await titleInput.fill(title)

    await page.getByRole('spinbutton', {
      name: 'Enter the transaction amount'
    }).fill('2500')

    await page.locator('select[name="type"]')
      .selectOption('INCOME')

    await page.getByRole('textbox', {
      name: 'Enter the transaction'
    }).fill('Ingreso creado por Playwright')

    await page.locator('select[name="categoryId"]')
      .selectOption(categoryId!)

    await page.getByRole('button', {
      name: 'Create transaction'
    }).click()

    // 6. Verificar el ingreso en pantalla
    await expect(page.getByText(title, { exact: true }))
      .toBeVisible()

    // 7. Verificar persistencia del ingreso
    await expect.poll(async () => {
      const list = await transactions(request, jwt)
      const saved = list.find(t => t.title === title)
      return saved ? Number(saved.amount) : null
    }, { timeout: 15_000 }).toBe(2500)

    console.log('Ingreso y categoría creados correctamente')

  } finally {
    const list = await transactions(request, jwt)
    const saved = list.find(t => t.title === title)

    if (saved) {
      await removeTransaction(request, jwt, saved.id)
    }

    if (!categoryId) {
      categoryId = (await categories(request, jwt))
        .find(c => c.name === categoryName)?.id
    }

    if (categoryId) {
      await removeCategory(request, jwt, categoryId)
    }
  }
})

test('crear un gasto y verlo en el dashboard', async ({ page, request }) => {
  const jwt = await token(request)
  const title = unique('e2e-gasto')
  const category = await createCategory(request, jwt)

  try {
    await login(page)

    // Crear el gasto desde Chromium
    await createExpenseUI(
      page,
      title,
      '1500',
      category.id
    )

    // Comprobar que aparece en el dashboard
    await expect(page.getByText(title, { exact: true }))
      .toBeVisible()

    // Comprobar que el importe quedó guardado en PostgreSQL
    await expect.poll(async () => {
      const list = await transactions(request, jwt)
      const saved = list.find(t => t.title === title)
      return saved ? Number(saved.amount) : null
    }, { timeout: 15_000 }).toBe(1500)

    console.log('Gasto creado correctamente')

  } finally {
    const list = await transactions(request, jwt)
    const saved = list.find(t => t.title === title)

    if (saved) {
      await removeTransaction(request, jwt, saved.id)
    }

    await removeCategory(request, jwt, category.id)
  }
})

test('eliminar un gasto desde la interfaz', async ({ page, request }) => {
  const jwt = await token(request)
  const title = unique('e2e-eliminar')
  const category = await createCategory(request, jwt)

  try {
    await login(page)

    // 1. Crear un gasto de prueba
    await createExpenseUI(
      page,
      title,
      '500',
      category.id
    )

    // 2. Comprobar que existe antes de eliminarlo
    await expect.poll(async () => {
      const list = await transactions(request, jwt)
      return list.some(t => t.title === title)
    }, { timeout: 15_000 }).toBe(true)

    // 3. Localizar la fila exacta
    const row = page.getByRole('row').filter({
      has: page.getByText(title, { exact: true })
    })

    await expect(row).toHaveCount(1)

    // 4. Eliminar desde la interfaz
    await row.getByRole('button', {
      name: 'Delete'
    }).click()

    // 5. Comprobar que desapareció de la BD
    await expect.poll(async () => {
      const list = await transactions(request, jwt)
      return list.some(t => t.title === title)
    }, { timeout: 15_000 }).toBe(false)

    // 6. Comprobar que desapareció de la pantalla
    await expect(page.getByText(title, { exact: true }))
      .toHaveCount(0)

    console.log('Gasto eliminado correctamente')

  } finally {
    const list = await transactions(request, jwt)
    const saved = list.find(t => t.title === title)

    if (saved) {
      await removeTransaction(request, jwt, saved.id)
    }

    await removeCategory(request, jwt, category.id)
  }
})
