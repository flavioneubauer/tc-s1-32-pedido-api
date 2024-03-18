package br.com.fiap.soat1.t32.utils;

import br.com.fiap.soat1.t32.models.entities.pedidos.Pedido;
import br.com.fiap.soat1.t32.models.entities.pedidos.PedidoProduto;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ClientePedidoVo;
import br.com.fiap.soat1.t32.models.parameters.pedidos.PedidoVo;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ProdutoPedidoVo;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ListaPedidosClienteData;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ListaPedidosData;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ListaPedidosProdutoData;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ListaPedidosResponse;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class PedidoMapper {

	private static Cliente toClienteEntity(ClientePedidoVo clientePedidoVo) {
		if (clientePedidoVo == null)
			return null;
		var cliente = new Cliente();
		cliente.setId(clientePedidoVo.getId());
		return cliente;
	}

	private static PedidoProduto toPedidoProdutoEntity(ProdutoPedidoVo produtoPedidoVo) {
		return PedidoProduto.builder()
				.quantidade(produtoPedidoVo.getQuantidade())
				.produto(toProdutoEntity(produtoPedidoVo))
				.build();
	}

	private static Produto toProdutoEntity(ProdutoPedidoVo produtoPedidoVo) {
		return Produto.builder()
				.id(produtoPedidoVo.getId())
				.build();
	}

	private static Set<PedidoProduto> toListaProdutoEntity(List<ProdutoPedidoVo> listaProdutoPedidoVo) {
		return listaProdutoPedidoVo.stream().map(PedidoMapper::toPedidoProdutoEntity).collect(Collectors.toSet());
	}

	public static Pedido toEntity(PedidoVo pedidoVo) {
		return Pedido.builder().produtos(toListaProdutoEntity(pedidoVo.getProdutos()))
				.cliente(toClienteEntity(pedidoVo.getCliente())).build();
	}

	public static ListaPedidosData toListaVo(Pedido pedido) {
		return ListaPedidosData.builder()
				.id(pedido.getId())
				.statusPreparacao(pedido.getStatusPreparacao())
				.produtos(toListaProdutosVo(pedido.getProdutos()))
				.cliente(toClienteVo(pedido.getCliente()))
				.build();
	}

	private static ListaPedidosClienteData toClienteVo(Cliente cliente) {
		return cliente == null ? null : ListaPedidosClienteData.builder()
				.nome(cliente.getNome())
				.id(cliente.getId())
				.build();
	}

	private static List<ListaPedidosProdutoData> toListaProdutosVo(Set<PedidoProduto> pedidoProdutos) {
		return pedidoProdutos.stream()
				.map(produto -> ListaPedidosProdutoData.builder()
						.categoria(produto.getProduto().getCategoria())
						.id(produto.getProduto().getId())
						.descricao(produto.getProduto().getDescricao())
						.quantidade(produto.getQuantidade())
						.build())
				.toList();
	}

	public static ListaPedidosResponse toListaResponse(List<Pedido> listaPedido) {
		return ListaPedidosResponse.builder()
				.pedidos(listaPedido.stream()
						.map(PedidoMapper::toListaVo)
						.toList()).build();
	}

}
