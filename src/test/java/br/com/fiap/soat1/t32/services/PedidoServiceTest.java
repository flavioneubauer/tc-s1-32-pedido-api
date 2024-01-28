package br.com.fiap.soat1.t32.services;

import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import br.com.fiap.soat1.t32.enums.StatusPreparacaoPedido;
import br.com.fiap.soat1.t32.exceptions.ValidationException;
import br.com.fiap.soat1.t32.models.entities.pedidos.Pedido;
import br.com.fiap.soat1.t32.models.entities.pedidos.PedidoProduto;
import br.com.fiap.soat1.t32.models.entities.pedidos.PedidoProdutoKey;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ClientePedidoVo;
import br.com.fiap.soat1.t32.models.parameters.pedidos.PedidoVo;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ProdutoPedidoVo;
import br.com.fiap.soat1.t32.repositories.pedidos.PedidoProdutoRepository;
import br.com.fiap.soat1.t32.repositories.pedidos.PedidoRepository;
import br.com.fiap.soat1.t32.repositories.pedidos.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PedidoServiceTest {

	@Mock
	private PedidoRepository pedidoRepository;

	@Mock
	private PedidoProdutoRepository pedidoProdutoRepository;

	@Mock
	private ProdutoRepository produtoRepository;

	@InjectMocks
	private PedidoService pedidoService;

	Pedido pedido;
	Produto produto;

	@BeforeEach
	void onBefore() {
		pedido = new Pedido();
		pedido.setId(1L);
		pedido.setCliente(Cliente.builder()
				.id(UUID.randomUUID())
				.build());

		produto = new Produto();
		produto.setId(1L);
		produto.setPreco(BigDecimal.valueOf(10));
		produto.setDescricao("xtudo");
		produto.setCategoria(CategoriaProduto.LANCHE);

		PedidoProduto pedidoProduto = new PedidoProduto();
		pedidoProduto.setId(PedidoProdutoKey.builder()
				.pedidoId(1L)
				.produtoId(1L)
				.build());
		pedidoProduto.setPedido(pedido);
		pedidoProduto.setProduto(produto);
		pedidoProduto.setQuantidade(1L);
		pedidoProduto.setValorUnitario(BigDecimal.valueOf(10));

		pedido.setProdutos(Stream.of(pedidoProduto)
				.collect(Collectors.toSet()));
	}

	@Test
	void testeCadastrarComSucesso() {
		when(pedidoRepository.save(any())).thenReturn(pedido);
		when(produtoRepository.findAllById(any())).thenReturn(Collections.singletonList(produto));

		PedidoVo pedidoVo = new PedidoVo();
		PedidoProduto pedidoProduto = pedido.getProdutos()
				.iterator()
				.next();
		ProdutoPedidoVo produtoPedidoVo = new ProdutoPedidoVo();
		produtoPedidoVo.setId(produto.getId());
		produtoPedidoVo.setQuantidade(pedidoProduto.getQuantidade());
		pedidoVo.setProdutos(Arrays.asList(produtoPedidoVo));
		pedidoVo.setCliente(new ClientePedidoVo());
		pedidoVo.getCliente()
				.setId(pedido.getCliente()
						.getId());

		assertEquals(1L, pedidoService.cadastrar(pedidoVo)
				.getId());
	}

	@Test
	void testeAlterarStatusPreparacaoDeNuloParaRecebido() {
		when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
		pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.RECEBIDO);
		assertEquals(StatusPreparacaoPedido.RECEBIDO, pedido.getStatusPreparacao());
	}

	@Test
	void testeAlterarStatusPreparacaoDeRecebidoParaEmPreparacao() {
		pedido.setStatusPreparacao(StatusPreparacaoPedido.RECEBIDO);
		when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
		pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.EM_PREPARACAO);
		assertEquals(StatusPreparacaoPedido.EM_PREPARACAO, pedido.getStatusPreparacao());
	}

	@Test
	void testeAlterarStatusPreparacaoDeEmPreparacaoParaPronto() {
		pedido.setStatusPreparacao(StatusPreparacaoPedido.EM_PREPARACAO);
		when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
		pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.PRONTO);
		assertEquals(StatusPreparacaoPedido.PRONTO, pedido.getStatusPreparacao());
	}

	@Test
	void testeAlterarStatusPreparacaoDeProntoParaFinalizado() {
		pedido.setStatusPreparacao(StatusPreparacaoPedido.PRONTO);
		when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
		pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.FINALIZADO);
		assertEquals(StatusPreparacaoPedido.FINALIZADO, pedido.getStatusPreparacao());
	}

	@Test
	void testeAlterarStatusPreparacaoInvalidoDeNuloParaOutrosQueNaoRecebido() {
		when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
		assertThrows(ValidationException.class, () -> pedidoService.alterarStatusPreparacaoPedido(1L,
				StatusPreparacaoPedido.EM_PREPARACAO));
		assertThrows(ValidationException.class,
				() -> pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.PRONTO));
		assertThrows(ValidationException.class,
				() -> pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.FINALIZADO));
	}

	@Test
	void testeAlterarStatusPreparacaoInvalidoDeRecebidoParaOutrosQueNaoEmPreparacao() {
		pedido.setStatusPreparacao(StatusPreparacaoPedido.RECEBIDO);
		when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
		assertThrows(ValidationException.class,
				() -> pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.PRONTO));
		assertThrows(ValidationException.class,
				() -> pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.FINALIZADO));
	}

	@Test
	void testeAlterarStatusPreparacaoInvalidoDeEmPreparacaoParaOutrosQueNaoPronto() {
		pedido.setStatusPreparacao(StatusPreparacaoPedido.EM_PREPARACAO);
		when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
		assertThrows(ValidationException.class,
				() -> pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.RECEBIDO));
		assertThrows(ValidationException.class,
				() -> pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.FINALIZADO));
	}

	@Test
	void testeAlterarStatusPreparacaoInvalidoDeProntoParaOutrosQueNaoFinalizado() {
		pedido.setStatusPreparacao(StatusPreparacaoPedido.PRONTO);
		when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));
		assertThrows(ValidationException.class,
				() -> pedidoService.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.RECEBIDO));
		assertThrows(ValidationException.class, () -> pedidoService.alterarStatusPreparacaoPedido(1L,
				StatusPreparacaoPedido.EM_PREPARACAO));
	}

	@Test
	void testeListar() {
		when(pedidoRepository.findByOrderByStatusPreparacaoAsc())
				.thenReturn(Stream.of(pedido).toList());

		assertEquals(1, pedidoService.listar().getPedidos()
				.size());
	}
}