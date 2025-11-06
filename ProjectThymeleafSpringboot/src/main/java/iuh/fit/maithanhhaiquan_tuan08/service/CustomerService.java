package iuh.fit.maithanhhaiquan_tuan08.service;

import iuh.fit.maithanhhaiquan_tuan08.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAll();

    Customer findById(Integer id);

    void save(Customer customer);

    void deleteById(Integer id);

    List<Customer> search(String keyword);

    Customer findByUsername(String username);
}