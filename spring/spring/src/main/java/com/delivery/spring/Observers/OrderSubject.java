package com.delivery.spring.Observers;

import com.delivery.spring.Models.Order;

public interface OrderSubject {
    void registerObserver(OrderObserver observer);
    void removeObserver(OrderObserver observer);
    void notifyObservers(Order order);
}
