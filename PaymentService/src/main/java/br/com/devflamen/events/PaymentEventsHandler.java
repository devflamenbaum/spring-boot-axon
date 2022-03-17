package br.com.devflamen.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.com.devflamen.common.events.PaymentProcessedEvent;
import br.com.devflamen.data.Payment;
import br.com.devflamen.data.PaymentRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentEventsHandler {
	
	private final PaymentRepository paymentRepository;
	
	public PaymentEventsHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
	
	@EventHandler
    public void on(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent is called for orderId: " + event.getOrderId());

        Payment payment = new Payment();
        BeanUtils.copyProperties(event, payment);

        paymentRepository.save(payment);

    }
}
