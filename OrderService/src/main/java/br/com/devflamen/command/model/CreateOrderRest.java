package br.com.devflamen.command.model;

import lombok.Data;

@Data
public class CreateOrderRest {
	
	private String productId;
	private Integer quantity;
	private String addressId;
}
