package br.com.devflamen.query.controller;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devflamen.common.data.User;
import br.com.devflamen.common.query.FetchUserPaymentDetailsQuery;

@RestController
@RequestMapping("/users")
public class UserQueryController {
	
	private final QueryGateway queryGateway;

	public UserQueryController(QueryGateway queryGateway) {
		this.queryGateway = queryGateway;
	}
	
	@GetMapping("/{userId}/payment-details")
	public User getUserPaymentDetails(@PathVariable String userId) {
		
		FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(userId);
		
		return queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
	}

}
