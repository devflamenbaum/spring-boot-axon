package br.com.devflamen.command.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devflamen.command.CreateProductCommand;
import br.com.devflamen.command.model.CreateProductRest;

@RestController
@RequestMapping("/products")
public class ProductCommandController {
	
	private final CommandGateway commandGateway;
	
	public ProductCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createProduct(@Valid @RequestBody CreateProductRest createProductRest) {

		CreateProductCommand createProductCommand = CreateProductCommand.builder()
																		.price(createProductRest.getPrice())
																		.title(createProductRest.getTitle())
																		.quantity(createProductRest.getQuantity())
																		.productId(UUID.randomUUID().toString())
																		.build();
		
		String returnValue;
		
		returnValue = commandGateway.sendAndWait(createProductCommand);
		
		/*
		 * try { returnValue = commandGateway.sendAndWait(createProductCommand); } catch
		 * (Exception e) { returnValue = e.getLocalizedMessage(); }
		 */
		
		return returnValue;
	}
}
