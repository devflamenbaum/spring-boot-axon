package br.com.devflamen.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import br.com.devflamen.core.enumeration.OrderStatus;
import br.com.devflamen.core.events.OrderApprovedEvent;
import br.com.devflamen.core.events.OrderCreatedEvent;
import br.com.devflamen.core.events.OrderRejectedEvent;

@Aggregate
public class OrderAggregate {
	
	@AggregateIdentifier
	private String orderId;
	private String userId;
	private String productId;
	private int quantity;
	private String addressId;
	private OrderStatus orderStatus;
	
	public OrderAggregate() {

	}
	
	@CommandHandler
	public OrderAggregate(CreateOrderCommand createOrderCommand) {
		
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
		BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
		
		AggregateLifecycle.apply(orderCreatedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderCreatedEvent event) {
		this.orderId = event.getOrderId();
		this.userId = event.getUserId();
		this.productId = event.getProductId();
		this.quantity = event.getQuantity();
		this.addressId = event.getAddressId();
		this.orderStatus = event.getOrderStatus();
	}
	
	@CommandHandler
	public void handle(ApproveOrderCommand approveOrderCommand) {
		
		OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approveOrderCommand.getOrderId());
		
		AggregateLifecycle.apply(orderApprovedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderApprovedEvent event) {
		this.orderStatus = event.getOrderStatus();
	}
	
	@CommandHandler
	public void handle(RejectedOrderCommand rejectedOrderCommand) {
		
		OrderRejectedEvent orderRejectedEvent = 
				new OrderRejectedEvent(rejectedOrderCommand.getOrderId(), rejectedOrderCommand.getReason());
		
		AggregateLifecycle.apply(orderRejectedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderRejectedEvent event) {
		this.orderStatus = event.getOrderStatus();
	}
	

}
