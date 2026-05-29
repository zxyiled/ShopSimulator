export interface Product {
  code: string
  name: string
  price: number
  quantity: number
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data: T
}
