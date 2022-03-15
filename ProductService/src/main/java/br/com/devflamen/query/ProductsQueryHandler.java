package br.com.devflamen.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.com.devflamen.core.data.Product;
import br.com.devflamen.core.repository.ProductRepository;
import br.com.devflamen.query.model.ProductRestModel;

@Component
public class ProductsQueryHandler {

	private final ProductRepository productRepository;

	public ProductsQueryHandler(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@QueryHandler
	public List<ProductRestModel> findProducts(FindProductsQuery query) {
		List<ProductRestModel> list = new ArrayList<>();

		List<Product> store = productRepository.findAll();

		list = store.stream().map(p -> {
			ProductRestModel prm = new ProductRestModel();
			BeanUtils.copyProperties(p, prm);
			return prm;
		}).collect(Collectors.toList());

		return list;
	}
}
