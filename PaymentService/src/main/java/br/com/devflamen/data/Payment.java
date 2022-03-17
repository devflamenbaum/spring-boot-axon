package br.com.devflamen.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class Payment implements Serializable {

	private static final long serialVersionUID = 7547302242806808152L;

	@Id
    private String paymentId;

    @Column
    public String orderId;

}
