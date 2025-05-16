package com.delivery.spring.Observers;

import com.delivery.spring.Models.Order;
import org.springframework.stereotype.Service;


@Service
public class EmailNotificationObserver implements OrderObserver {

    @Override
    public void update(Order order) {
        String emailContent = buildEmailContent(order);
        String customerEmail = order.getCustomer().getEmail();


        // В реальном приложении здесь был бы вызов email-сервиса
        // emailService.send(customerEmail, "Ваш заказ #" + order.getOrderId(), emailContent);
    }

    private String buildEmailContent(Order order) {
        return String.format(
                "Уважаемый %s,\n\n" +
                        "Ваш заказ #%d успешно оформлен!\n" +
                        "Сумма заказа: %d руб.\n" +
                        "Способ оплаты: %s\n" +
                        "Адрес доставки: %s\n\n" +
                        "Статус: %s\n\n" +
                        "Спасибо за покупку!",
                order.getCustomer().getName(),
                order.getOrderId(),
                order.getTotalPrice(),
                order.getPaymentMethod().toString(),
                order.getDeliveryAddress(),
                order.getStatus()
        );
    }
}