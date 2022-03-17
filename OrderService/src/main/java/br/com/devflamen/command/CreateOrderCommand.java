package br.com.devflamen.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.devflamen.core.enumeration.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderCommand {
	
	@TargetAggregateIdentifier
	public String orderId;
	private String userId;
	private String productId;
	private int quantity;
	private String addressId;
	private OrderStatus orderStatus;
}
