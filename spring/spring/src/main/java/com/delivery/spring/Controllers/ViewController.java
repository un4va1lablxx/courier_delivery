package com.delivery.spring.Controllers;

import com.delivery.spring.CartService;
import com.delivery.spring.Models.*;
import com.delivery.spring.Observers.WebNotificationObserver;
import com.delivery.spring.OrderService;
import com.delivery.spring.Repositories.*;
import com.delivery.spring.Strategies.PaymentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private WebNotificationObserver webNotificationObserver;
    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    @PostMapping("/cart/removeFromCart")
    public String removeFromCart(@RequestParam Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }
    @GetMapping("/menu")
    public String showMenu(Model model) {
        List<Product> products = productRepository.findAll();
        Map<String, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));
        model.addAttribute("productsByCategory", productsByCategory);
        return "index-2";
    }
    @GetMapping("/cart")
    public String showCart(Model model) {
        List<OrderItem> cartItems = cartService.getCartItems();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "index-3";
    }
    @PostMapping("/cart/addToCart")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        cartService.addProductToCart(productId, quantity);
        return "redirect:/cart"; // После добавления перенаправляем в корзину
    }
    @PostMapping("/cart/checkout")
    public String checkout(Authentication authentication,
                           @RequestParam String address,
                           @RequestParam PaymentMethod paymentMethod,
                           Model model) {
        Customer customer = customerRepository.findByLogin(authentication.getName());

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setTotalPrice(cartService.getTotalPrice());
        order.setDateOrdered(new Timestamp(System.currentTimeMillis()));

        Courier courier = courierRepository.findRandomCourierByStatus("Активен");
        order.setCourier(courier);

        PaymentResult paymentResult = cartService.checkout(order);
        model.addAttribute("paymentResult", paymentResult);

        if (paymentResult.isSuccess()) {
            order.setStatus("Оплачено");
            Order savedOrder = orderService.saveOrder(order);
            model.addAttribute("orderId", savedOrder.getOrderId());
            model.addAttribute("showSuccessNotification", true);
            // Добавляем paymentMethod в модель
            model.addAttribute("paymentMethod", paymentMethod);
            cartService.moveCartItemsToOrder(savedOrder);
        } else {
            order.setStatus("Ошибка оплаты");
            orderRepository.save(order);
            model.addAttribute("showPaymentError", true);
        }

        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());

        return "index-3";
    }
    @GetMapping("/orders/{orderId}")
    public String viewOrder(@PathVariable("orderId") Long orderId, Model model) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));

        model.addAttribute("order", order);
        return "order-details";
    }
    @GetMapping("/contacts")
    public String showContacts() {
        return "index-4";
    }

}