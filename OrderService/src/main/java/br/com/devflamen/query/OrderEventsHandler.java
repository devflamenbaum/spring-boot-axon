package br.com.devflamen.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.com.devflamen.core.data.Order;
import br.com.devflamen.core.events.OrderApprovedEvent;
import br.com.devflamen.core.events.OrderCreatedEvent;
import br.com.devflamen.core.events.OrderRejectedEvent;
import br.com.devflamen.core.repository.OrderRepository;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {
	
	private final OrderRepository orderRepository;
	
	public OrderEventsHandler(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@EventHandler
	public void on(OrderCreatedEvent orderCreatedEvent) {
		
		Order order = new Order();
		BeanUtils.copyProperties(orderCreatedEvent, order);
		
		orderRepository.save(order);
		
	}
	
	@EventHandler
	public void on(OrderApprovedEvent orderApprovedEvent) {
		
		Order order = orderRepository.findByOrderId(orderApprovedEvent.getOrderId());
		
		if(order == null) {
			return;
		}
		
		order.setOrderStatus(orderApprovedEvent.getOrderStatus());
		
		orderRepository.save(order);
		
	}
	
	@EventHandler
	public void on(OrderRejectedEvent orderRejectedEvent) {
		
		Order order = orderRepository.findByOrderId(orderRejectedEvent.getOrderId());
		
		if(order == null) {
			return;
		}
		
		order.setOrderStatus(orderRejectedEvent.getOrderStatus());
		
		orderRepository.save(order);
		
	}
}
