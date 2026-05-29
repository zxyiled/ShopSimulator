import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { getCurrentUser, getProducts, logout, registerProduct, Unauthorized } from '../api'
import type { Product } from '../types'

const emptyForm = { code: '', name: '', price: '', quantity: '' }

export default function Dashboard() {
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [products, setProducts] = useState<Product[]>([])
  const [message, setMessage] = useState<{ text: string; ok: boolean } | null>(null)
  const [form, setForm] = useState(emptyForm)

  async function loadProducts() {
    setProducts(await getProducts())
  }

  useEffect(() => {
    void (async () => {
      try {
        setUsername(await getCurrentUser())
        await loadProducts()
      } catch (err) {
        if (err instanceof Unauthorized) navigate('/login', { replace: true })
      }
    })()
  }, [])

  async function onAddProduct(e: FormEvent) {
    e.preventDefault()
    setMessage(null)
    try {
      const res = await registerProduct({
        code: form.code,
        name: form.name,
        price: Number(form.price),
        quantity: Number(form.quantity),
      })
      setMessage({ text: res.message, ok: res.success })
      if (res.success) {
        setForm(emptyForm)
        await loadProducts()
      }
    } catch (err) {
      if (err instanceof Unauthorized) navigate('/login', { replace: true })
    }
  }

  async function onLogout() {
    await logout()
    navigate('/login?logout', { replace: true })
  }

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <div>
          <h1>Inventory Dashboard</h1>
          <p>
            Welcome, <span id="current-user">{username}</span>
          </p>
        </div>
        <button id="logout-btn" type="button" onClick={onLogout}>
          Logout
        </button>
      </div>

      <div className="panel">
        <h2>Add product</h2>
        <form id="add-product-form" className="add-product-form" onSubmit={onAddProduct}>
          <div className="field">
            <label htmlFor="product-code">Code</label>
            <input
              id="product-code"
              name="code"
              value={form.code}
              onChange={(e) => setForm({ ...form, code: e.target.value })}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="product-name">Name</label>
            <input
              id="product-name"
              name="name"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="product-price">Price</label>
            <input
              id="product-price"
              name="price"
              type="number"
              step="0.01"
              value={form.price}
              onChange={(e) => setForm({ ...form, price: e.target.value })}
              required
            />
          </div>
          <div className="field">
            <label htmlFor="product-stock">Stock</label>
            <input
              id="product-stock"
              name="quantity"
              type="number"
              value={form.quantity}
              onChange={(e) => setForm({ ...form, quantity: e.target.value })}
              required
            />
          </div>
          <button id="add-product-btn" type="submit">
            Add
          </button>
        </form>
        {message && (
          <div id="message" className={`message ${message.ok ? 'ok' : 'fail'}`}>
            {message.text}
          </div>
        )}
      </div>

      <table id="inventory-table">
        <thead>
          <tr>
            <th>Code</th>
            <th>Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((p) => (
            <tr key={p.code} data-testid={`product-row-${p.code}`}>
              <td>{p.code}</td>
              <td>{p.name}</td>
              <td>{p.price.toFixed(2)}</td>
              <td>{p.quantity}</td>
              <td></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
