package com.delivery.spring;

import com.delivery.spring.Models.Order;
import com.delivery.spring.Observers.OrderObserver;
import com.delivery.spring.Observers.OrderSubject;
import com.delivery.spring.Repositories.OrderRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements OrderSubject {
    private List<OrderObserver> observers = new ArrayList<>();
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void registerObserver(OrderObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Order order) {
        for (OrderObserver observer : observers) {
            observer.update(order);
        }
    }

    public Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        notifyObservers(savedOrder); // Уведомляем наблюдателей
        return savedOrder;
    }
}