package br.com.devflamen.common.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import br.com.devflamen.common.data.PaymentDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessPaymentCommand {
	
	@TargetAggregateIdentifier
	private final String paymentId;
	private final String orderId;
	private final PaymentDetails paymentDetails;

}
