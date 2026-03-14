/**
 * api.js — Centralized HTTP client for the Inventory REST API.
 *
 * All fetch calls go through the `request` function so that the base URL,
 * headers, and error handling are defined in a single place.
 */

const BASE_URL = '/api';

/**
 * Generic request helper.
 * @param {string} path   - e.g. '/products' or '/products/ABC123/stock'
 * @param {string} method - HTTP verb
 * @param {object|null} body  - will be JSON-serialized if provided
 * @returns {Promise<object>} parsed ApiResponse from the backend
 */
async function request(path, method = 'GET', body = null) {
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' },
    };
    if (body) options.body = JSON.stringify(body);

    const response = await fetch(BASE_URL + path, options);
    return response.json();
}

// ── Products ───────────────────────────────────────────────────────────────

const ProductAPI = {
    getAll() {
        return request('/products');
    },

    getByCode(code) {
        return request(`/products/${encodeURIComponent(code)}`);
    },

    register(code, name, price, quantity) {
        return request('/products', 'POST', { code, name, price, quantity });
    },

    updateStock(code, operation, quantity) {
        return request(`/products/${encodeURIComponent(code)}/stock`, 'PATCH', { operation, quantity });
    },

    validateStock(code, qty) {
        return request(`/products/${encodeURIComponent(code)}/validate?qty=${qty}`);
    },
};

// ── Alerts ─────────────────────────────────────────────────────────────────

const AlertAPI = {
    getAll() {
        return request('/alerts');
    },

    clear() {
        return request('/alerts', 'DELETE');
    },
};

// ── Stats ──────────────────────────────────────────────────────────────────

const StatsAPI = {
    get() {
        return request('/stats');
    },
};