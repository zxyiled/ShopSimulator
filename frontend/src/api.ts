import type { ApiResponse, Product } from './types'

export class Unauthorized extends Error {}

export async function login(username: string, password: string): Promise<boolean> {
  const res = await fetch('/api/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: new URLSearchParams({ username, password }),
  })
  return res.ok
}

export async function logout(): Promise<void> {
  await fetch('/api/logout', { method: 'POST' })
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
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  })
  if (res.status === 401) throw new Unauthorized()
  return (await res.json()) as ApiResponse<null>
}
