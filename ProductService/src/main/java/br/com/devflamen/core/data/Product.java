package br.com.devflamen.core.data;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;


}
