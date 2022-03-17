package br.com.devflamen.core.events;

import br.com.devflamen.core.enumeration.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {
	
	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.APPROVED;
	

}
