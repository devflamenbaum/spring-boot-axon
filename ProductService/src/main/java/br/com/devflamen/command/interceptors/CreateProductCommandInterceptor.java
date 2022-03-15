package br.com.devflamen.command.interceptors;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import br.com.devflamen.command.CreateProductCommand;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
			List<? extends CommandMessage<?>> messages) {
		
		return (index, command) -> {
			
			log.info("Intercepted command: {}", command.getPayload());
			
			if(CreateProductCommand.class.equals(command.getPayload().getClass())) {
				
				CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
				
				if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
					throw new IllegalArgumentException("Price cannot be less or equal than 0");
				}
				
				if(createProductCommand.getTitle() == null || createProductCommand.getTitle().isBlank()) {
					throw new IllegalArgumentException("Title cannot be empty");
				}
			}
			return command;
		};
	}

}
