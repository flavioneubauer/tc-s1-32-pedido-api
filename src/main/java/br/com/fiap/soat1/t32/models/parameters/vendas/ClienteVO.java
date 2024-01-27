package br.com.fiap.soat1.t32.models.parameters.vendas;

import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteVO {

	@NotNull
	private String nome;
	
	@NotNull
	private String cpf;
	
	@NotNull
	private String email;

	public Cliente toCliente() {
		return Cliente.builder()
			.nome(this.nome)
			.cpf(this.cpf.replace(".", "").replace("-", ""))
			.email(this.email)
			.build();
	}
}