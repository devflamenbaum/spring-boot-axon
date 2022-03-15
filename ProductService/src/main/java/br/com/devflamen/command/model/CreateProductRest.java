package br.com.devflamen.command.model;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateProductRest {
	
	//@NotBlank(message = "Product is a required Field")
	private String title;
	
	@Min(value = 0, message = "Price cannot be lower than 0")
	private BigDecimal price;
	@Min(value = 1, message = "Quantity cannot be lower than 0")
	@Max(value = 99, message = "Quantity cannot be greater than 99")
	private Integer quantity;
}
