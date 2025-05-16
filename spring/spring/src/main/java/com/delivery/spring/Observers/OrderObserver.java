package com.delivery.spring.Observers;

import com.delivery.spring.Models.Order;

public interface OrderObserver {
    void update(Order order);
}
