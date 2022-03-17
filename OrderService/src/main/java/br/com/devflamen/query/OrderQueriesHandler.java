package br.com.devflamen.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import br.com.devflamen.core.data.Order;
import br.com.devflamen.core.data.OrderSummary;
import br.com.devflamen.core.repository.OrderRepository;

@Component
public class OrderQueriesHandler {
	
	private final OrderRepository orderRepository;
	
	public OrderQueriesHandler(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}


	@QueryHandler
	public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
		Order order = orderRepository.findByOrderId(findOrderQuery.getOrderId());
		return new OrderSummary(order.getOrderId(), order.getOrderStatus(), "");
	}
}
