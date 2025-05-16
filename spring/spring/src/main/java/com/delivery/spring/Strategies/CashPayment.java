package com.delivery.spring.Strategies;

import org.springframework.stereotype.Service;

@Service
public class CashPayment implements PaymentStrategy {
    @Override
    public PaymentResult pay(int amount, String description) {
        // Для наличных всегда успешно
        return new PaymentResult(true, "CASH-" + System.currentTimeMillis(),
                "Ожидается оплата наличными при получении");
    }
}