package com.delivery.spring.Observers;

import com.delivery.spring.OrderService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {
    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailNotificationObserver emailObserver;

    @Autowired
    private WebNotificationObserver webNotificationObserver;

    @PostConstruct
    public void registerObservers() {
        orderService.registerObserver(emailObserver);
        orderService.registerObserver(webNotificationObserver);
    }
}
