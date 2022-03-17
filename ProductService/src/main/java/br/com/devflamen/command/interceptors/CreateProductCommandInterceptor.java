package br.com.devflamen.command.interceptors;

import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import br.com.devflamen.command.CreateProductCommand;
import br.com.devflamen.core.data.ProductLookup;
import br.com.devflamen.core.repository.ProductLookupRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
	
	private final ProductLookupRepository productLookupRepository;

	public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}

	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
			List<? extends CommandMessage<?>> messages) {
		
		return (index, command) -> {
			
			log.info("Intercepted command: {}", command.getPayload());
			
			if(CreateProductCommand.class.equals(command.getPayload().getClass())) {
				
				CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
				
				ProductLookup productLookup = productLookupRepository
						.findByProductIdOrTitle(createProductCommand.getProductId(), createProductCommand.getTitle());
				
				if(productLookup != null) {
					throw new IllegalStateException(
							String.format("Product with productId %s or tittle %s already exist", 
									createProductCommand.getProductId(), createProductCommand.getTitle()));
				}
			}
			return command;
		};
	}

}
