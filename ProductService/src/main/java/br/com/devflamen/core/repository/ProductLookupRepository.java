package br.com.devflamen.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.devflamen.core.data.ProductLookup;

public interface ProductLookupRepository extends JpaRepository<ProductLookup, String> {
	
	ProductLookup findByProductIdOrTitle(String productId, String title);

}
