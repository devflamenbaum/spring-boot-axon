package br.com.devflamen.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.devflamen.query.FindProductsQuery;
import br.com.devflamen.query.model.ProductRestModel;

@RestController
@RequestMapping("/products")
public class ProductQueryController {
	
	private final QueryGateway queryGateway;
	
	public ProductQueryController(QueryGateway queryGateway) {
		this.queryGateway = queryGateway;
	}

	@GetMapping
	public List<ProductRestModel> getProducts(){
		
		FindProductsQuery query = new FindProductsQuery();
		List<ProductRestModel> products = queryGateway.query(query, 
												ResponseTypes.multipleInstancesOf(ProductRestModel.class))
												.join();
		
		return products;
	}
}
