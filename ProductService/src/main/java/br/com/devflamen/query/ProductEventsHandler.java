package br.com.devflamen.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.com.devflamen.common.events.ProductReservationCancelledEvent;
import br.com.devflamen.common.events.ProductReservedEvent;
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
		
		try {
			productRepository.save(product);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) {
		
		Product product = productRepository.findByProductId(productReservedEvent.getProductId());
		product.setQuantity(product.getQuantity() - productReservedEvent.getQuantity());
		
		try {
			productRepository.save(product);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void on(ProductReservationCancelledEvent productReservationCancelledEvent) {
		
		Product product = productRepository.findByProductId(productReservationCancelledEvent.getProductId());
		
		int newQuantity = product.getQuantity() + productReservationCancelledEvent.getQuantity();
		
		product.setQuantity(newQuantity);
		
		try {
			productRepository.save(product);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	@ResetHandler
	public void reset() {
		productRepository.deleteAll();
	}
	
	@ExceptionHandler(resultType = IllegalArgumentException.class)
	public void handle(IllegalArgumentException ex) {
		//Log a message
	}
	
	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception ex) throws Exception {
		throw ex;
	}
}
