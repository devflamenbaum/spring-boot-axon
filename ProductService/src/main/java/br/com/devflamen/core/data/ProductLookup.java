package br.com.devflamen.core.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productslookup")
public class ProductLookup implements Serializable{

	private static final long serialVersionUID = -1444658500949455993L;
	
	@Id
	private String productId;
	private String title;

}
