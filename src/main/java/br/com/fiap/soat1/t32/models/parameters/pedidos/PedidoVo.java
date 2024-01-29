package br.com.fiap.soat1.t32.models.parameters.pedidos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class PedidoVo {
	@NotEmpty
	private List<ProdutoPedidoVo> produtos;
	private ClientePedidoVo cliente;
}
