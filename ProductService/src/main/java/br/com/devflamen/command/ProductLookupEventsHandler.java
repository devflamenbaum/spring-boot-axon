package br.com.devflamen.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.springframework.stereotype.Component;

import br.com.devflamen.core.data.ProductLookup;
import br.com.devflamen.core.events.ProductCreatedEvent;
import br.com.devflamen.core.repository.ProductLookupRepository;

@Component
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {
	
	private final ProductLookupRepository productLookupRepository;
	
	public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}

	@EventHandler
	public void on(ProductCreatedEvent event) {
		
		ProductLookup productLookUp = new ProductLookup(event.getProductId(), event.getTitle());
		
		productLookupRepository.save(productLookUp);
		
	}
	
	@ResetHandler
	public void reset() {
		productLookupRepository.deleteAll();
	}
}
