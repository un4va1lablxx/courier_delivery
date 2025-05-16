package com.delivery.spring.Strategies;

import org.springframework.stereotype.Service;

@Service
public class OnlinePayment implements PaymentStrategy {
    @Override
    public PaymentResult pay(int amount, String description) {
        // Имитация онлайн платежа
        boolean paymentSuccess = Math.random() > 0.7; // 30% успешных платежей

        if (paymentSuccess) {
            String transactionId = "ONL-" + System.currentTimeMillis();
            return new PaymentResult(true, transactionId, "Онлайн платеж выполнен");
        } else {
            return new PaymentResult(false, null, "Ошибка онлайн платежа");
        }
    }
}