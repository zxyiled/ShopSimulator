import { useState, type FormEvent } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { login } from '../api'

export default function Login() {
  const navigate = useNavigate()
  const [params] = useSearchParams()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(false)
  const loggedOut = params.has('logout')

  async function onSubmit(e: FormEvent) {
    e.preventDefault()
    setError(false)
    const ok = await login(username, password)
    if (ok) {
      navigate('/dashboard', { replace: true })
    } else {
      setError(true)
    }
  }

  return (
    <div className="login-wrapper">
      <div className="login-card">
        <h1>ShopSimulator - Login</h1>
        {error && (
          <div className="error" id="error-message">
            Invalid credentials
          </div>
        )}
        {loggedOut && !error && (
          <div className="notice" id="logout-message">
            You have been logged out
          </div>
        )}
        <form onSubmit={onSubmit}>
          <div className="field">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              name="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
            />
          </div>
          <div className="field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
            />
          </div>
          <button id="login-btn" type="submit">
            Log in
          </button>
        </form>
      </div>
    </div>
  )
}
