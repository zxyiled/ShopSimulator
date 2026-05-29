import type { ApiResponse, Product } from './types'

export class Unauthorized extends Error {}

function readCookie(name: string): string | undefined {
  const match = document.cookie
    .split('; ')
    .find((c) => c.startsWith(name + '='))
  return match ? decodeURIComponent(match.split('=')[1]) : undefined
}

function csrfHeader(): Record<string, string> {
  const token = readCookie('XSRF-TOKEN')
  return token ? { 'X-XSRF-TOKEN': token } : {}
}

async function ensureCsrfToken(): Promise<void> {
  if (!readCookie('XSRF-TOKEN')) {
    await fetch('/api/me')
  }
}

export async function login(username: string, password: string): Promise<boolean> {
  await ensureCsrfToken()
  const res = await fetch('/api/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded', ...csrfHeader() },
    body: new URLSearchParams({ username, password }),
  })
  return res.ok
}

export async function logout(): Promise<void> {
  await fetch('/api/logout', { method: 'POST', headers: { ...csrfHeader() } })
}

export async function getCurrentUser(): Promise<string> {
  const res = await fetch('/api/me')
  if (res.status === 401) throw new Unauthorized()
  const data = (await res.json()) as { username: string }
  return data.username
}

export async function getProducts(): Promise<Product[]> {
  const res = await fetch('/api/products')
  if (res.status === 401) throw new Unauthorized()
  const body = (await res.json()) as ApiResponse<Product[]>
  return body.data ?? []
}

export interface ProductInput {
  code: string
  name: string
  price: number
  quantity: number
}

export async function registerProduct(input: ProductInput): Promise<ApiResponse<null>> {
  const res = await fetch('/api/products', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', ...csrfHeader() },
    body: JSON.stringify(input),
  })
  if (res.status === 401) throw new Unauthorized()
  return (await res.json()) as ApiResponse<null>
}

export async function updateStock(
  code: string,
  operation: 'augment' | 'reduce',
  quantity: number,
): Promise<ApiResponse<Product | null>> {
  const res = await fetch(`/api/products/${encodeURIComponent(code)}/stock`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json', ...csrfHeader() },
    body: JSON.stringify({ operation, quantity }),
  })
  if (res.status === 401) throw new Unauthorized()
  return (await res.json()) as ApiResponse<Product | null>
}
