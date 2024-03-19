package br.com.fiap.soat1.t32.services;

import br.com.fiap.soat1.t32.enums.StatusPreparacaoPedido;
import br.com.fiap.soat1.t32.exceptions.ValidationException;
import br.com.fiap.soat1.t32.models.entities.pedidos.Pedido;
import br.com.fiap.soat1.t32.models.entities.pedidos.PedidoProduto;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import br.com.fiap.soat1.t32.models.presenters.pedidos.CriacaoPedidoRequest;
import br.com.fiap.soat1.t32.models.presenters.pedidos.CriacaoPedidoResponse;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ListaPedidosResponse;
import br.com.fiap.soat1.t32.repositories.pedidos.PedidoProdutoRepository;
import br.com.fiap.soat1.t32.repositories.pedidos.PedidoRepository;
import br.com.fiap.soat1.t32.repositories.pedidos.ProdutoRepository;
import br.com.fiap.soat1.t32.utils.PedidoMapper;
import br.com.fiap.soat1.t32.utils.PedidoProdutoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final ProdutoRepository produtoRepository;
	private final PedidoProdutoRepository pedidoProdutoRepository;

	@Transactional
	public CriacaoPedidoResponse cadastrar(CriacaoPedidoRequest criacaoPedidoRequest) {
		var pedido = PedidoMapper.toEntity(criacaoPedidoRequest.getPedido());
		pedido = pedidoRepository.save(pedido);

		var produtos = getProdutos(pedido.getProdutos());
		var pedidoProdutosEntity = PedidoProdutoMapper.toEntity(produtos, pedido.getProdutos(), pedido.getId());

		pedidoProdutoRepository.saveAll(pedidoProdutosEntity);

		return CriacaoPedidoResponse.builder().idPedido(pedido.getId())
				.idPagamento(criacaoPedidoRequest.getIdPagamento())
				.build();
	}

	public void alterarStatusPreparacaoPedido(Long id, StatusPreparacaoPedido statusPreparacaoPedido) {
		validarPreparacaoAlteracao(id, statusPreparacaoPedido);

		var pedido = pedidoRepository.findById(id);
		pedido.ifPresent(pedidoEntity -> {
			pedidoEntity.setStatusPreparacao(statusPreparacaoPedido);
			pedidoRepository.save(pedidoEntity);
		});
	}

	public ListaPedidosResponse listar() {
		return PedidoMapper.toListaResponse(pedidoRepository.findByOrderByStatusPreparacaoAsc());
	}

	private Map<Long, Produto> getProdutos(Set<PedidoProduto> pedidoProdutos) {
		var produtos = produtoRepository.findAllById(pedidoProdutos.stream()
				.map(pedidoProduto -> pedidoProduto.getProduto().getId())
				.collect(Collectors.toSet()));

		var produtosMap = new HashMap<Long, Produto>();

		produtos.forEach(produto -> produtosMap.put(produto.getId(), produto));

		return produtosMap;
	}

	private void validarPreparacaoAlteracao(Long idPedido, StatusPreparacaoPedido status) {
		Optional<Pedido> pedidoOptional = pedidoRepository.findById(idPedido);

		if(pedidoOptional.isEmpty()) {
			throw new ValidationException("Pedido não localizado.");
		} else{
			Pedido pedido = pedidoOptional.get();
			if(isNull(pedido.getStatusPreparacao()) &&
					StatusPreparacaoPedido.RECEBIDO != status) {
				throw new ValidationException("Pedido sem status de preparação só pode ser alterado para RECEBIDO.");
			} else if(StatusPreparacaoPedido.RECEBIDO == pedido.getStatusPreparacao() &&
					StatusPreparacaoPedido.EM_PREPARACAO != status) {
				throw new ValidationException("Pedido RECEBIDO só pode ser alterado para EM PREPARAÇÃO.");
			} else if(StatusPreparacaoPedido.EM_PREPARACAO == pedido.getStatusPreparacao() &&
					StatusPreparacaoPedido.PRONTO != status) {
				throw new ValidationException("Pedido EM PREPARAÇÃO só pode ser alterado para PRONTO.");
			} else if(StatusPreparacaoPedido.PRONTO == pedido.getStatusPreparacao() &&
					StatusPreparacaoPedido.FINALIZADO != status) {
				throw new ValidationException("Pedido PRONTO só pode ser alterado para FINALIZADO.");
			}
		}
	}

}
