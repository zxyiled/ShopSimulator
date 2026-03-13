/**
 * navigation.js — Handles sidebar navigation and page switching.
 *
 * Each "page" is a <section> in index.html that gets shown/hidden.
 * The active nav button gets the .active class.
 */

/**
 * Switches to the given page and triggers its load function.
 * @param {string} page - one of: 'products' | 'register' | 'stock' | 'alerts'
 */
function navigate(page) {
    // Hide all pages
    document.querySelectorAll('section[id^="page-"]')
        .forEach(s => s.classList.add('hidden'));

    // Deactivate all nav buttons
    document.querySelectorAll('.nav-btn')
        .forEach(b => b.classList.remove('active'));

    // Show the selected page and activate its button
    document.getElementById('page-' + page).classList.remove('hidden');
    document.querySelector(`[data-page="${page}"]`).classList.add('active');

    // Trigger page-specific load logic
    switch (page) {
        case 'products': loadProductsPage(); break;
        case 'alerts':   loadAlertsPage();   break;
        case 'stock':
            document.getElementById('lookupPreview').textContent =
                'Enter a product code and press Look up.';
            break;
    }
}