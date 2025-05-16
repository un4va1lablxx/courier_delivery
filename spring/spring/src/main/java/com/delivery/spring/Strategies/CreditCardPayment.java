package com.delivery.spring.Strategies;

import org.springframework.stereotype.Service;

@Service
public class CreditCardPayment implements PaymentStrategy {
    @Override
    public PaymentResult pay(int amount, String description) {
        // Имитация обработки кредитной карты
        boolean paymentSuccess = Math.random() > 0.5; // 50% успешных платежей

        if (paymentSuccess) {
            String transactionId = "CC-" + System.currentTimeMillis();
            return new PaymentResult(true, transactionId, "Оплата картой прошла успешно");
        } else {
            return new PaymentResult(false, null, "Ошибка оплаты картой: недостаточно средств");
        }
    }
}
