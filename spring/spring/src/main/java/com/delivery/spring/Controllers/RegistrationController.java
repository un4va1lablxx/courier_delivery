package com.delivery.spring.Controllers;

import com.delivery.spring.Models.Customer;
import com.delivery.spring.Repositories.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private CustomerRepository customerRepository;
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping
    public String registerCustomer(@Valid @ModelAttribute Customer customer, Model model) {
        if (!customer.getPassword().equals(customer.getConfirmPassword())) {
            model.addAttribute("error", "Пароли не совпадают.");
            return "register";
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return "redirect:/login";
    }
}