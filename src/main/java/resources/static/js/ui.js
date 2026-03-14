/**
 * ui.js — Shared DOM utility functions.
 *
 * These helpers are used by all page modules (products, register, stock, alerts)
 * so they live here instead of being duplicated.
 */

/**
 * Sets a feedback label with a success or error style.
 * @param {HTMLElement} el
 * @param {string}      message
 * @param {boolean}     success
 */
function setFeedback(el, message, success) {
    el.textContent = message;
    el.className = 'feedback ' + (success ? 'ok' : 'err');
}

/**
 * Updates the alert badge count on the sidebar nav button.
 * Removes the badge entirely when count is 0.
 * @param {number} count
 */
function updateAlertBadge(count) {
    const btn = document.getElementById('navAlerts');
    const existing = btn.querySelector('.badge');
    if (existing) existing.remove();

    btn.textContent = '⚑  Alerts';

    if (count > 0) {
        const badge = document.createElement('span');
        badge.className = 'badge';
        badge.textContent = count;
        btn.appendChild(badge);
    }
}

/**
 * Returns a status pill HTML string based on a product's quantity.
 * @param {number} quantity
 * @returns {string} HTML string
 */
function statusPill(quantity) {
    return quantity <= 5
        ? `<span class="pill badge-warn">LOW STOCK</span>`
        : `<span class="pill badge-ok">OK</span>`;
}