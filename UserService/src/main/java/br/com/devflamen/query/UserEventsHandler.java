package br.com.devflamen.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import br.com.devflamen.common.data.PaymentDetails;
import br.com.devflamen.common.data.User;
import br.com.devflamen.common.query.FetchUserPaymentDetailsQuery;

@Component
public class UserEventsHandler {

	@QueryHandler
	public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {

		PaymentDetails paymentDetails = PaymentDetails.builder().cardNumber("123456").cvv("123")
				.name("GABRIEL FLAMENBAUM").validUntilMonth(12).validUntilYear(2030).build();

		User user = User.builder().firstName("Gabriel").lastName("Flamenbaum").userId(query.getUserId())
				.paymentDetails(paymentDetails).build();

		return user;
	}
}
