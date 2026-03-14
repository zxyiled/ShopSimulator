/**
 * register.js — Logic for the Register Product form page.
 *
 * Responsibilities:
 *  - Validate inputs client-side before sending
 *  - Call the API to register a new product
 *  - Show feedback and clear the form on success
 */

/**
 * Reads the form, validates, and calls the register API.
 * Bound to the "Register Product" button's onclick.
 */
async function registerProduct() {
    const code  = document.getElementById('regCode').value.trim();
    const name  = document.getElementById('regName').value.trim();
    const price = parseFloat(document.getElementById('regPrice').value);
    const qty   = parseInt(document.getElementById('regQty').value);
    const fb    = document.getElementById('regFeedback');

    // Client-side pre-validation
    if (isNaN(price)) return setFeedback(fb, '✕  Price must be a valid number.', false);
    if (isNaN(qty))   return setFeedback(fb, '✕  Quantity must be a whole number.', false);

    const res = await ProductAPI.register(code, name, price, qty);

    setFeedback(fb, (res.success ? '✓  ' : '✕  ') + res.message, res.success);

    if (res.success) clearRegForm();
}

/**
 * Clears all inputs on the register form.
 * Bound to the "Clear" button's onclick.
 */
function clearRegForm() {
    ['regCode', 'regName', 'regPrice', 'regQty']
        .forEach(id => { document.getElementById(id).value = ''; });
}