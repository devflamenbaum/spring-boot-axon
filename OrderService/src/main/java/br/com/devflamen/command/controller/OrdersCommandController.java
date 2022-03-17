package br.com.devflamen.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devflamen.command.CreateOrderCommand;
import br.com.devflamen.command.model.CreateOrderRest;
import br.com.devflamen.core.data.OrderSummary;
import br.com.devflamen.core.enumeration.OrderStatus;
import br.com.devflamen.query.FindOrderQuery;

@RestController
@RequestMapping("/orders")
public class OrdersCommandController {
	
	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;
	
	public OrdersCommandController(CommandGateway commandGateway,QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}

	@PostMapping
	public OrderSummary createOrder(@RequestBody CreateOrderRest createOrderRest) {
		
		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
																.orderId(UUID.randomUUID().toString())
																.productId(createOrderRest.getProductId())
																.addressId(createOrderRest.getAddressId())
																.quantity(createOrderRest.getQuantity())
																.userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
																.orderStatus(OrderStatus.CREATED)
																.build();
		
		SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = 
				queryGateway.subscriptionQuery(new FindOrderQuery(createOrderCommand.getOrderId()), 
												ResponseTypes.instanceOf(OrderSummary.class) , 
												ResponseTypes.instanceOf(OrderSummary.class));
		
		commandGateway.sendAndWait(createOrderCommand);
		
		try {
			return queryResult.updates().blockFirst();
		} finally {
			queryResult.close();
		}
		
	}
}
