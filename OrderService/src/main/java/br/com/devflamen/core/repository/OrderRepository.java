package br.com.devflamen.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devflamen.core.data.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
	
	Order findByOrderId(String orderId);

}
