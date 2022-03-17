package br.com.devflamen.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import br.com.devflamen.common.commands.ProcessPaymentCommand;
import br.com.devflamen.common.data.PaymentDetails;
import br.com.devflamen.common.events.PaymentProcessedEvent;


@Aggregate
public class PaymentAggregate {
	
	@AggregateIdentifier
	private String paymentId;
	private String orderId;
	private PaymentDetails paymentDetails;
	
	public PaymentAggregate() {
		// TODO Auto-generated constructor stub
	}
	
	@CommandHandler
	public PaymentAggregate(ProcessPaymentCommand command) {
		
		if(command.getPaymentDetails() == null) {
    		throw new IllegalArgumentException("Missing payment details");
    	}
    	
    	if(command.getOrderId() == null) {
    		throw new IllegalArgumentException("Missing orderId");
    	}
    	
    	if(command.getPaymentId() == null) {
    		throw new IllegalArgumentException("Missing paymentId");
    	}
	
        AggregateLifecycle.apply(new PaymentProcessedEvent(command.getOrderId(), 
                command.getPaymentId()));
    }
	
	@EventSourcingHandler
    protected void on(PaymentProcessedEvent paymentProcessedEvent){
        this.paymentId = paymentProcessedEvent.getPaymentId();
        this.orderId = paymentProcessedEvent.getOrderId();
    }

}
