package br.com.devflamen.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RejectedOrderCommand {
	
	@TargetAggregateIdentifier
	private final String orderId;
	private final String reason;
}
