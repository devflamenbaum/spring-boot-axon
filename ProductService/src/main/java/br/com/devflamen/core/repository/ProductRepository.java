package br.com.devflamen.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devflamen.core.data.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
	
	Product findByProductId(String productId);
	Product findByProductIdOrTitle(String productId, String title);
}
