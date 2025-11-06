package iuh.fit.maithanhhaiquan_tuan08.service.impl;

import iuh.fit.maithanhhaiquan_tuan08.entity.Customer;
import iuh.fit.maithanhhaiquan_tuan08.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements iuh.fit.maithanhhaiquan_tuan08.service.CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> search(String keyword) {
        return customerRepository.search(keyword);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByName(username);
    }

}