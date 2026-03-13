/**
 * stock.js — Logic for the Stock Management page.
 *
 * Responsibilities:
 *  - Product lookup by code
 *  - Augment stock
 *  - Reduce stock
 *  - Validate inventory availability
 */

/**
 * Looks up a product by the code entered in #stockCode and
 * displays a summary line in #lookupPreview.
 */
async function lookupProduct() {
    const code = document.getElementById('stockCode').value.trim();
    const prev = document.getElementById('lookupPreview');

    if (!code) {
        prev.textContent = 'Enter a product code and press Look up.';
        prev.style.color = 'var(--muted)';
        return;
    }

    const res = await ProductAPI.getByCode(code);

    if (res.success && res.data) {
        const p = res.data;
        prev.textContent =
            `Found: ${p.name}  |  Code: ${p.code}  |  Stock: ${p.quantity}  |  Price: $${p.price.toFixed(2)}`;
        prev.style.color = 'var(--success)';
    } else {
        prev.textContent = `✕  Product with code "${code}" not found.`;
        prev.style.color = 'var(--danger)';
    }
}

/**
 * Increases stock for the looked-up product.
 */
async function augmentStock() {
    const code = document.getElementById('stockCode').value.trim();
    const qty  = parseInt(document.getElementById('augQty').value);
    const fb   = document.getElementById('augFeedback');

    if (isNaN(qty) || qty <= 0)
        return setFeedback(fb, '✕  Enter a valid quantity greater than 0.', false);

    const res = await ProductAPI.updateStock(code, 'augment', qty);
    setFeedback(fb, (res.success ? '✓  ' : '✕  ') + res.message, res.success);

    if (res.success) {
        document.getElementById('augQty').value = '';
        lookupProduct(); // refresh the preview with the new stock value
    }
}

/**
 * Decreases stock for the looked-up product.
 */
async function reduceStock() {
    const code = document.getElementById('stockCode').value.trim();
    const qty  = parseInt(document.getElementById('redQty').value);
    const fb   = document.getElementById('redFeedback');

    if (isNaN(qty) || qty <= 0)
        return setFeedback(fb, '✕  Enter a valid quantity greater than 0.', false);

    const res = await ProductAPI.updateStock(code, 'reduce', qty);
    setFeedback(fb, (res.success ? '✓  ' : '✕  ') + res.message, res.success);

    if (res.success) {
        document.getElementById('redQty').value = '';
        lookupProduct();
    }
}

/**
 * Checks whether a product has enough stock for a required quantity.
 */
async function validateStock() {
    const code = document.getElementById('valCode').value.trim();
    const qty  = parseInt(document.getElementById('valQty').value);
    const fb   = document.getElementById('valFeedback');

    if (isNaN(qty) || qty <= 0)
        return setFeedback(fb, '✕  Enter a valid required quantity.', false);

    const res = await ProductAPI.validateStock(code, qty);
    setFeedback(fb, (res.success ? '✓  ' : '✕  ') + res.message, res.success);
}