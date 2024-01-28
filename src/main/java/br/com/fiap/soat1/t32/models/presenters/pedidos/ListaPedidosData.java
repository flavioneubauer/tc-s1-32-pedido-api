package br.com.fiap.soat1.t32.models.presenters.pedidos;

import br.com.fiap.soat1.t32.enums.StatusPagamentoPedido;
import br.com.fiap.soat1.t32.enums.StatusPreparacaoPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaPedidosData {
    private Long id;
    private StatusPreparacaoPedido statusPreparacao;
    private StatusPagamentoPedido statusPagamento;
    private ListaPedidosClienteData cliente;
    private List<ListaPedidosProdutoData> produtos;

}
