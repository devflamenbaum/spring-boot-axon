package br.com.devflamen.common.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchUserPaymentDetailsQuery {
	
	private String userId;
}
