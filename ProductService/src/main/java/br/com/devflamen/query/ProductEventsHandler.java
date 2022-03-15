package br.com.devflamen.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.com.devflamen.core.data.Product;
import br.com.devflamen.core.events.ProductCreatedEvent;
import br.com.devflamen.core.repository.ProductRepository;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {
	
	private final ProductRepository productRepository;	
	
	public ProductEventsHandler(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		
		Product product = new Product();
		BeanUtils.copyProperties(productCreatedEvent, product);
		
		productRepository.save(product);
	}
}
