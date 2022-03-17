package br.com.devflamen.saga;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.devflamen.command.ApproveOrderCommand;
import br.com.devflamen.command.RejectedOrderCommand;
import br.com.devflamen.common.commands.CancelProductReservationCommand;
import br.com.devflamen.common.commands.ProcessPaymentCommand;
import br.com.devflamen.common.commands.ReserveProductCommand;
import br.com.devflamen.common.data.User;
import br.com.devflamen.common.events.PaymentProcessedEvent;
import br.com.devflamen.common.events.ProductReservationCancelledEvent;
import br.com.devflamen.common.events.ProductReservedEvent;
import br.com.devflamen.common.query.FetchUserPaymentDetailsQuery;
import br.com.devflamen.core.data.OrderSummary;
import br.com.devflamen.core.events.OrderApprovedEvent;
import br.com.devflamen.core.events.OrderCreatedEvent;
import br.com.devflamen.core.events.OrderRejectedEvent;
import br.com.devflamen.query.FindOrderQuery;
import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderSaga {
	
	@Autowired
	private transient CommandGateway commandGateway;
	
	@Autowired
	private transient QueryGateway queryGateway;
	
	@Autowired
	private transient DeadlineManager deadlineManager;
	
	@Autowired
	private transient QueryUpdateEmitter queryUpdateEmitter;
	
	private final String PAYMENT_PROCESSING_TIMEOUT = "payment-processing-deadline";
	
	private String deadlineId;
	
	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		
		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
																.orderId(orderCreatedEvent.getOrderId())
																.quantity(orderCreatedEvent.getQuantity())
																.productId(orderCreatedEvent.getProductId())
																.userId(orderCreatedEvent.getUserId())
																.build();
		log.info("OrderCreatedEvent handled for orderId and productId: {} and {}", 
				reserveProductCommand.getOrderId(), reserveProductCommand.getProductId());
		
		commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
			if(commandResultMessage.isExceptional()) {
				//Compensate transaction
				RejectedOrderCommand rejectedOrderCommand = 
						new RejectedOrderCommand(reserveProductCommand.getOrderId(), commandResultMessage.exceptionResult().getMessage());
				
				commandGateway.send(rejectedOrderCommand);
			}
		});
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		//process User payment
		log.info("ProductReservedEvent is called for productId: {}", productReservedEvent.getProductId());
		
		FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
		
		User userPaymentDetails = null;
		
		try {
			
			userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
			
		} catch (Exception e) {
			//compensating transactions
			log.error(e.getMessage());
			cancelProductReservation(productReservedEvent, e.getMessage());
			
			return;
		}
		
		if(userPaymentDetails == null) {
			//compensation transactions
			cancelProductReservation(productReservedEvent, "could not fetch User Payment Details");
			return;
		}
		
		log.info("Successfully fetched user payment details for userId: {}", userPaymentDetails.getUserId());
		
		deadlineId = deadlineManager.schedule(Duration.of(10, ChronoUnit.SECONDS), PAYMENT_PROCESSING_TIMEOUT, productReservedEvent);
		
		ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
				.orderId(productReservedEvent.getOrderId())
				.paymentId(UUID.randomUUID().toString())
				.paymentDetails(userPaymentDetails.getPaymentDetails())
				.build();
		
		String result = null;
		
		try {
			
			result = commandGateway.sendAndWait(processPaymentCommand);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			//compensating transaction
			cancelProductReservation(productReservedEvent, e.getMessage());
			return;
		}
		
		if(result == null) {
			log.info("The process Payment is Null. Start Compensating transaction");
			// start compensation transaction
			cancelProductReservation(productReservedEvent, "Could not process user payment with provided payment details");
		}
	}
	
	private void cancelProductReservation(ProductReservedEvent event, String reason) {
		
		cancelDeadline();
		
		CancelProductReservationCommand cancelProductReservationCommand = 
				CancelProductReservationCommand.builder()
								.orderId(event.getOrderId())
								.productId(event.getProductId())
								.quantity(event.getQuantity())
								.userId(event.getUserId())
								.reason(reason)
								.build();
		
		commandGateway.send(cancelProductReservationCommand);
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		
		cancelDeadline();
		
		ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
		
		commandGateway.send(approveOrderCommand);
	}
	
	private void cancelDeadline() {
		
		if(deadlineId != null) {
			deadlineManager.cancelSchedule(PAYMENT_PROCESSING_TIMEOUT, deadlineId);
			deadlineId = null;
		}
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
		
		RejectedOrderCommand rejectedOrderCommand = 
				new RejectedOrderCommand(productReservationCancelledEvent.getOrderId(), productReservationCancelledEvent.getReason());
		
		commandGateway.send(rejectedOrderCommand);
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderRejectedEvent orderRejectedEvent) {
		
		log.info("Order has been rejected. Reason: {} ", orderRejectedEvent.getReason());
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, 
				new OrderSummary(orderRejectedEvent.getOrderId(), orderRejectedEvent.getOrderStatus(), orderRejectedEvent.getReason()));
		//SagaLifecycle.end();
	}
	
	@EndSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		log.info("Order Approved. Order saga is complete");
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, 
				new OrderSummary(orderApprovedEvent.getOrderId(), orderApprovedEvent.getOrderStatus(), "Order Approved"));
	}
	
	@DeadlineHandler(deadlineName = PAYMENT_PROCESSING_TIMEOUT)
	public void handlePaymentDeadline(ProductReservedEvent productReservedEvent) {
		log.info("Payment Processing deadline took place. Sending a compensating command");
		cancelProductReservation(productReservedEvent, "Payment timeout");
	}

}
