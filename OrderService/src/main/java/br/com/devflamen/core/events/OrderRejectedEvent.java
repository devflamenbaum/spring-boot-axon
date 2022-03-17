package br.com.devflamen.core.events;

import br.com.devflamen.core.enumeration.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {
	
	private final String orderId;
	private final String reason;
	private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
