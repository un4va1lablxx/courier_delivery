package com.delivery.spring;

import com.delivery.spring.Models.Courier;
import com.delivery.spring.Models.Customer;
import com.delivery.spring.Repositories.CourierRepository;
import com.delivery.spring.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CourierRepository courierRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByLogin(login);
        if (customer != null) {
            return new User(
                    customer.getLogin(),
                    customer.getPassword(),
                    Collections.emptyList()
            );
        }
        Courier courier = courierRepository.findByLogin(login);
        if (courier != null) {
            return new User(
                    courier.getLogin(),
                    courier.getPassword(),
                    Collections.emptyList()
            );
        }
        throw new UsernameNotFoundException("User not found with login: " + login);
    }

}
