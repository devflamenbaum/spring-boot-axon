package br.com.devflamen.core.data;

import br.com.devflamen.core.enumeration.OrderStatus;
import lombok.Value;

@Value
public class OrderSummary {
	
	private final String orderId;
	private final OrderStatus orderStatus;
	private final String message;
}
