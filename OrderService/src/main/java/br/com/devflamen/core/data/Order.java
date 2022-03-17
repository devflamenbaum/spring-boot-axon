package br.com.devflamen.core.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.devflamen.core.enumeration.OrderStatus;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order implements Serializable {
	
	private static final long serialVersionUID = -5790883234932525807L;
	
	@Id
	private String orderId;
	private String userId;
	private String productId;
	private int quantity;
	private String addressId;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

}
