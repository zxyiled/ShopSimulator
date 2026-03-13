/**
 * products.js — Logic for the Products listing page.
 *
 * Responsibilities:
 *  - Load and render the product table
 *  - Update dashboard stats
 *  - Client-side search/filter
 */

// In-memory cache of the full product list for client-side filtering
let allProducts = [];

/**
 * Loads products and stats from the API, then renders the page.
 * Called on navigation and after a new product is registered.
 */
async function loadProductsPage() {
    const [prodRes, statsRes] = await Promise.all([
        ProductAPI.getAll(),
        StatsAPI.get(),
    ]);

    allProducts = prodRes.data ?? [];
    renderProductTable(allProducts);

    if (statsRes.data) {
        document.getElementById('statTotal').textContent = statsRes.data.totalProducts;
        document.getElementById('statLow').textContent   = statsRes.data.lowStockCount;
        document.getElementById('statValue').textContent =
            '$' + Number(statsRes.data.totalValue).toFixed(0);
        updateAlertBadge(statsRes.data.totalAlerts ?? 0);
    }
}

/**
 * Renders rows into the product table.
 * @param {Array} products
 */
function renderProductTable(products) {
    const tbody = document.getElementById('productTableBody');

    if (!products.length) {
        tbody.innerHTML = `
      <tr class="empty-row">
        <td colspan="5">No products registered yet. Use "Register" to add your first product.</td>
      </tr>`;
        return;
    }

    tbody.innerHTML = products.map(p => `
    <tr>
      <td class="td-mono">${p.code}</td>
      <td>${p.name}</td>
      <td class="${p.quantity <= 5 ? 'qty-lo' : 'qty-ok'} td-mono">${p.quantity}</td>
      <td class="price">$${p.price.toFixed(2)}</td>
      <td>${statusPill(p.quantity)}</td>
    </tr>
  `).join('');
}

/**
 * Filters the cached product list by code or name.
 * Bound to the search input's oninput event.
 * @param {string} query
 */
function filterTable(query) {
    const q = query.toLowerCase();
    const filtered = q
        ? allProducts.filter(p =>
            p.code.toLowerCase().includes(q) ||
            p.name.toLowerCase().includes(q)
        )
        : allProducts;

    renderProductTable(filtered);
}