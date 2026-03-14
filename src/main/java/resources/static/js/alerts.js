/**
 * alerts.js — Logic for the Alerts page.
 *
 * Responsibilities:
 *  - Load and render low-stock alert cards
 *  - Handle "Clear All Alerts" action
 */

/**
 * Fetches low-stock products and renders the alerts page.
 * Called on navigation to the Alerts page.
 */
async function loadAlertsPage() {
    const res = await AlertAPI.getAll();
    const products = res.data ?? [];

    updateAlertCount(products.length);
    updateAlertBadge(products.length);
    renderAlertsList(products);
}

/**
 * Clears all alerts via the API and refreshes the page.
 */
async function clearAlerts() {
    await AlertAPI.clear();
    loadAlertsPage();
}

// ── Private helpers ────────────────────────────────────────────────────────

function updateAlertCount(count) {
    document.getElementById('alertCount').textContent =
        count + (count === 1 ? ' active alert' : ' active alerts');
}

function renderAlertsList(products) {
    const list = document.getElementById('alertsList');

    if (!products.length) {
        list.innerHTML = `
      <div class="empty-alerts">
        <div class="empty-icon">✓</div>
        <div class="empty-msg">All clear! No low-stock alerts.</div>
        <div class="empty-sub">Products with 5 or fewer units will appear here.</div>
      </div>`;
        return;
    }

    list.innerHTML = products.map(buildAlertRow).join('');
}

function buildAlertRow(p) {
    const isOut  = p.quantity === 0;
    const color  = isOut ? 'var(--danger)' : 'var(--warning)';
    const badge  = isOut
        ? `<span class="pill badge-danger">OUT OF STOCK</span>`
        : `<span class="pill badge-warn">LOW STOCK</span>`;

    return `
    <div class="alert-row">
      <div class="alert-bar" style="background:${color}"></div>
      <div class="alert-info">
        <div class="alert-name">${p.name}</div>
        <div class="alert-meta">Code: ${p.code} · Price: $${p.price.toFixed(2)}</div>
      </div>
      ${badge}
      <div class="alert-stock">
        <div class="alert-stock-val" style="color:${color}">${p.quantity}</div>
        <div class="alert-stock-lbl">units left</div>
      </div>
    </div>`;
}