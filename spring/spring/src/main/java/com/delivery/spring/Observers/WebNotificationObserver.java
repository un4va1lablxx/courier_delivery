package com.delivery.spring.Observers;

import com.delivery.spring.Models.Order;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebNotificationObserver implements OrderObserver {
    private final Map<Long, List<String>> userNotifications = new ConcurrentHashMap<>();

    @Override
    public void update(Order order) {
        String notification = String.format(
                "Заказ #%d от %s на сумму %d руб. %s",
                order.getOrderId(),
                DateTimeFormatter.ISO_LOCAL_DATE.format(order.getDateOrdered().toLocalDateTime()),
                order.getTotalPrice(),
                getStatusMessage(order.getStatus())
        );

        addNotification(order.getCustomer().getCustomerId(), notification);
    }

    private String getStatusMessage(String status) {
        return switch (status) {
            case "Оплачено" -> "успешно оплачен";
            case "Ошибка оплаты" -> "не оплачен (ошибка)";
            default -> "получен";
        };
    }

    public void addNotification(Long userId, String message) {
        userNotifications.computeIfAbsent(userId, k -> new ArrayList<>()).add(message);
    }

    public List<String> getNotifications(Long userId) {
        return userNotifications.getOrDefault(userId, Collections.emptyList());
    }

    public void clearNotifications(Long userId) {
        userNotifications.remove(userId);
    }
}