package com.delivery.spring;
import com.delivery.spring.Models.Order;
import com.delivery.spring.Models.OrderItem;
import com.delivery.spring.Models.Product;
import com.delivery.spring.Strategies.*;
import com.delivery.spring.Repositories.OrderItemRepository;
import com.delivery.spring.Repositories.OrderRepository;
import com.delivery.spring.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    private PaymentStrategy paymentStrategy;
    @Autowired
    private CreditCardPayment creditCardPayment;
    @Autowired
    private CashPayment cashPayment;
    @Autowired
    private OnlinePayment onlinePayment;
    private final Map<Long, OrderItem> cart = new HashMap<>();
    public List<OrderItem> getCartItems() {
        return List.copyOf(cart.values());
    }
    public int getTotalPrice() {
        return cart.values().stream()
                .mapToInt(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
    }
    public void addProductToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продукт не найден!"));
        OrderItem orderItem = cart.get(productId);
        if (orderItem == null) {
            orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            cart.put(productId, orderItem);
        } else {
            orderItem.setQuantity(orderItem.getQuantity() + quantity);
        }
    }
    public void removeFromCart(Long productId) {
        cart.remove(productId);
    }
    public void clearCart() {
        cart.clear();
    }
    public void moveCartItemsToOrder(Order order) {
        for (OrderItem orderItem : cart.values()) {
            orderItemRepository.save(orderItem);
            orderItem.setOrder(order);
        }
        orderRepository.save(order);
        clearCart();
    }
    public void setPaymentStrategy(PaymentStrategy paymentStrategy)
    {
        this.paymentStrategy = paymentStrategy;
    }
    public PaymentResult checkout(Order order) {
        PaymentStrategy strategy = switch(order.getPaymentMethod()) {
            case CREDIT_CARD -> creditCardPayment;
            case CASH -> cashPayment;
            case ONLINE -> onlinePayment;
        };

        return strategy.pay(
                order.getTotalPrice(),
                "Оплата заказа #" + order.getOrderId()
        );
    }

}
