const API_URL = '/api/products';

const form = document.getElementById('product-form');
const formTitle = document.getElementById('form-title');
const idField = document.getElementById('product-id');
const barcodeField = document.getElementById('barcode');
const nameField = document.getElementById('name');
const descriptionField = document.getElementById('description');
const priceField = document.getElementById('price');
const quantityField = document.getElementById('quantity');
const cancelBtn = document.getElementById('cancel-btn');
const tableBody = document.getElementById('product-table-body');

const scanInput = document.getElementById('scan-input');
const scanStatus = document.getElementById('scan-status');

async function loadProducts() {
  const res = await fetch(API_URL);
  const products = await res.json();
  tableBody.innerHTML = '';
  products.forEach(p => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${p.barcode}</td>
      <td>${p.name}</td>
      <td>${p.description ?? ''}</td>
      <td>${p.price}</td>
      <td>${p.quantity}</td>
      <td>
        <button data-action="edit" data-id="${p.id}">Edit</button>
        <button data-action="delete" data-id="${p.id}">Delete</button>
      </td>`;
    tableBody.appendChild(tr);
  });
}

function resetForm() {
  form.reset();
  idField.value = '';
  formTitle.textContent = 'Add product';
  cancelBtn.hidden = true;
}

function fillForm(product) {
  idField.value = product.id;
  barcodeField.value = product.barcode;
  nameField.value = product.name;
  descriptionField.value = product.description ?? '';
  priceField.value = product.price;
  quantityField.value = product.quantity;
  formTitle.textContent = `Edit product #${product.id}`;
  cancelBtn.hidden = false;
}

form.addEventListener('submit', async (e) => {
  e.preventDefault();
  const payload = {
    barcode: barcodeField.value.trim(),
    name: nameField.value.trim(),
    description: descriptionField.value.trim(),
    price: parseFloat(priceField.value),
    quantity: parseInt(quantityField.value, 10),
  };

  const id = idField.value;
  const res = await fetch(id ? `${API_URL}/${id}` : API_URL, {
    method: id ? 'PUT' : 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    alert(err.message || 'Request failed');
    return;
  }

  resetForm();
  loadProducts();
});

cancelBtn.addEventListener('click', resetForm);

tableBody.addEventListener('click', async (e) => {
  const btn = e.target.closest('button');
  if (!btn) return;
  const id = btn.dataset.id;

  if (btn.dataset.action === 'edit') {
    const res = await fetch(`${API_URL}/${id}`);
    if (res.ok) fillForm(await res.json());
  }

  if (btn.dataset.action === 'delete') {
    if (!confirm('Delete this product?')) return;
    await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    loadProducts();
  }
});

// USB barcode scanners behave like a keyboard: they type the code then send Enter.
scanInput.addEventListener('keydown', async (e) => {
  if (e.key !== 'Enter') return;
  e.preventDefault();

  const barcode = scanInput.value.trim();
  scanInput.value = '';
  if (!barcode) return;

  const res = await fetch(`${API_URL}/barcode/${encodeURIComponent(barcode)}`);
  if (res.ok) {
    const product = await res.json();
    fillForm(product);
    scanStatus.textContent = `Found: ${product.name}`;
    scanStatus.className = 'ok';
  } else {
    resetForm();
    barcodeField.value = barcode;
    scanStatus.textContent = 'Not found — fill in details to add it';
    scanStatus.className = 'error';
  }
});

loadProducts();
