package br.com.fiap.soat1.t32.utils.mappers;

import br.com.fiap.soat1.t32.models.entities.pedidos.Pedido;
import br.com.fiap.soat1.t32.models.entities.pedidos.PedidoProduto;
import br.com.fiap.soat1.t32.models.entities.pedidos.PedidoProdutoKey;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class PedidoProdutoAdapter {

    public static List<PedidoProduto> toEntity(Map<Long, Produto> produtos,
                                                   Set<PedidoProduto> pedidoProdutos,
                                                   Long pedidoId) {
        return pedidoProdutos.stream()
                .map(pedidoProduto -> {
                    final var produto = produtos.get(pedidoProduto.getProduto().getId());

                    return PedidoProduto.builder()
                            .id(PedidoProdutoKey.builder()
                                    .pedidoId(pedidoId)
                                    .produtoId(produto.getId())
                                    .build())
                            .pedido(Pedido.builder().id(pedidoId).build())
                            .produto(produto)
                            .quantidade(pedidoProduto.getQuantidade())
                            .valorUnitario(produto.getPreco())
                            .build();
                    }
                )
                .toList();
    }
}
