package br.com.fiap.soat1.t32.models.presenters.pedidos;

import br.com.fiap.soat1.t32.models.parameters.pedidos.PedidoVo;
import lombok.Data;

@Data
public class CriacaoPedidoRequest {
	private Long idPagamento;
	private PedidoVo pedido;
}
