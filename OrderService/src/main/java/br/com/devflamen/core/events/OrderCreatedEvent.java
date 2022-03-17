package br.com.devflamen.core.events;

import br.com.devflamen.core.enumeration.OrderStatus;
import lombok.Data;

@Data
public class OrderCreatedEvent {
	
	private String orderId;
	private String userId;
	private String productId;
	private int quantity;
	private String addressId;
	private OrderStatus orderStatus;

}
