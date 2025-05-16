package com.delivery.spring.Strategies;

public interface PaymentStrategy {
    PaymentResult pay(int amount, String description);
}
