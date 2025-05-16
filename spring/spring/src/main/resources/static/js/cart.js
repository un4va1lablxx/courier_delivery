document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.add-to-cart-btn').forEach(button => {
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-product-id');
            const productName = this.getAttribute('data-product-name');
            const productPrice = parseFloat(this.getAttribute('data-product-price')); // Сделаем цену числом

            // Получаем корзину из localStorage или создаем новый массив
            let cart = JSON.parse(localStorage.getItem('cart') || '[]');

            // Проверим, есть ли уже этот товар в корзине
            const existingProductIndex = cart.findIndex(item => item.id === productId);
            if (existingProductIndex >= 0) {
                // Если товар уже есть в корзине, увеличиваем его количество
                cart[existingProductIndex].quantity += 1;
            } else {
                // Если товара нет в корзине, добавляем новый товар
                cart.push({
                    id: productId,
                    name: productName,
                    price: productPrice,
                    quantity: 1
                });
            }

            // Сохраняем обновленную корзину в localStorage
            localStorage.setItem('cart', JSON.stringify(cart));

            // Обновляем таблицу на странице корзины
            updateCartTable();
        });
    });
});

function updateCartTable() {
    const cartItemsTable = document.getElementById('cart-items');
    const totalPriceElement = document.getElementById('total-price');
    cartItemsTable.innerHTML = ''; // Очищаем таблицу перед обновлением

    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    let totalPrice = 0;

    cart.forEach((item, index) => {
        const row = cartItemsTable.insertRow();

        const nameCell = row.insertCell();
        nameCell.textContent = item.name;

        const priceCell = row.insertCell();
        priceCell.textContent = item.price.toFixed(2) + ' ₽'; // Форматируем цену с двумя знаками после запятой

        const quantityCell = row.insertCell();
        const quantityInput = document.createElement('input');
        quantityInput.type = 'number';
        quantityInput.min = 1;
        quantityInput.value = item.quantity;
        quantityInput.classList.add('quantity-input');
        quantityInput.setAttribute('data-index', index);
        quantityInput.addEventListener('change', (event) => {
            updateQuantity(event.target.getAttribute('data-index'), event.target.value);
        });
        quantityCell.appendChild(quantityInput);

        const totalCell = row.insertCell();
        const itemTotal = item.price * item.quantity;
        totalCell.textContent = itemTotal.toFixed(2) + ' ₽'; // Форматируем общий итог

        const actionsCell = row.insertCell();
        const removeButton = document.createElement('button');
        removeButton.textContent = 'Удалить';
        removeButton.classList.add('remove-button');
        removeButton.setAttribute('data-index', index);
        removeButton.addEventListener('click', (event) => {
            removeItem(event.target.getAttribute('data-index'));
        });
        actionsCell.appendChild(removeButton);

        totalPrice += itemTotal;
    });

    totalPriceElement.textContent = totalPrice.toFixed(2) + ' ₽'; // Форматируем общую цену
}

function updateQuantity(index, quantity) {
    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    if (quantity > 0) {
        cart[index].quantity = parseInt(quantity);
        localStorage.setItem('cart', JSON.stringify(cart));
    } else {
        removeItem(index);
    }
    updateCartTable();
}

function removeItem(index) {
    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    cart.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartTable();
}