package br.com.fiap.soat1.t32.services;

import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import br.com.fiap.soat1.t32.exceptions.ValidationException;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ProdutoVo;
import br.com.fiap.soat1.t32.models.presenters.pedidos.CriacaoProdutoResponse;
import br.com.fiap.soat1.t32.repositories.pedidos.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProdutoServiceTest {

	@Mock
	private ProdutoRepository produtoRepository;

	@InjectMocks
	private ProdutoService produtoService;

	Produto produto;
	ProdutoVo produtoVo;

	@BeforeEach
	void onBefore() {
		produto = new Produto();
		produtoVo = new ProdutoVo();
		produto.setId(1L);
		produto.setPreco(BigDecimal.valueOf(10));
		produto.setDescricao("xtudo");
		produto.setCategoria(CategoriaProduto.LANCHE);
		BeanUtils.copyProperties(produto, produtoVo);
	}

	@Test
	void testeCadastroComSucesso() {
		when(produtoRepository.save(any())).thenReturn(produto);

		CriacaoProdutoResponse respostaProdutoCadastrado = produtoService.cadastrar(produtoVo);

		assertEquals(produto.getId(), respostaProdutoCadastrado.getId());
	}

	@Test
	void testeCadastroSemCategoria() {
		produtoVo.setCategoria(null);
		assertThrows(ValidationException.class, () -> produtoService.cadastrar(produtoVo));
	}

	@Test
	void testeCadastroSemDescricao() {
		produtoVo.setDescricao(null);
		assertThrows(ValidationException.class, () -> produtoService.cadastrar(produtoVo));
	}

	@Test
	void testeCadastroPrecoInvalido() {
		produtoVo.setPreco(null);
		assertThrows(ValidationException.class, () -> produtoService.cadastrar(produtoVo));
		produtoVo.setPreco(BigDecimal.ZERO);
		assertThrows(ValidationException.class, () -> produtoService.cadastrar(produtoVo));
	}

	@Test
	void testeExcluirComSucesso() {
		when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

		assertDoesNotThrow(() -> produtoService.excluir(produto.getId()));
	}

	@Test
	void testeExcluirProdutoInvalido() {
		when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());
		assertThrows(ValidationException.class, () -> produtoService.excluir(1L));
	}

	@Test
	void testeEditarComSucesso() {
		when(produtoRepository.findById(produto.getId()))
				.thenReturn(Optional.of(produto));
		produtoVo.setPreco(BigDecimal.valueOf(20));
		assertDoesNotThrow(() -> produtoService.editar(produto.getId(), produtoVo));
		assertEquals(produto.getPreco(), produtoVo.getPreco());
	}

	@Test
	void testeEditarProdutoInvalido() {
		when(produtoRepository.findById(produto.getId())).thenReturn(Optional.empty());
		assertThrows(ValidationException.class, () -> produtoService.editar(1L, produtoVo));
	}

	@Test
	void listarPorCategoria() {
		when(produtoRepository.findAllByCategoria(produto.getCategoria()))
				.thenReturn(Stream.of(produto).collect(Collectors.toSet()));

		Set<Produto> produtos = produtoService.listarPorCategoria(produto.getCategoria());
		assertEquals(1, produtos.size());
	}
}