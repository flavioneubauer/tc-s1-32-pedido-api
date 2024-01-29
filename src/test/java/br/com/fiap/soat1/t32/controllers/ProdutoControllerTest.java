package br.com.fiap.soat1.t32.controllers;

import br.com.fiap.soat1.t32.TestRedisConfiguration;
import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ProdutoVo;
import br.com.fiap.soat1.t32.models.presenters.pedidos.CriacaoProdutoResponse;
import br.com.fiap.soat1.t32.services.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static br.com.fiap.soat1.t32.TestHelper.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = TestRedisConfiguration.class)
class ProdutoControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private ProdutoService produtoService;

	@Autowired
	private RedisTemplate<CategoriaProduto, Set<Produto>> redisTemplate;

	private ProdutoVo produtoVo;

	@BeforeEach
	void onBefore(){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

		produtoVo = new ProdutoVo();
		produtoVo.setCategoria(CategoriaProduto.LANCHE);
		produtoVo.setDescricao("X-burger");
		produtoVo.setPreco(BigDecimal.valueOf(10.5));
		Arrays.stream(CategoriaProduto.values())
				.forEach(categoriaProduto -> redisTemplate.opsForValue().set(categoriaProduto, new HashSet<>()));
	}

	@Test
	void criarProduto() throws Exception{

		when(produtoService.cadastrar(produtoVo)).thenReturn(CriacaoProdutoResponse.builder().id(1L).build());

		mockMvc.perform(post("/v1/produtos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(produtoVo)))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andReturn();
	}


	@Test
	void editarProduto() throws Exception {
		mockMvc.perform(put("/v1/produtos/" + 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(produtoVo)))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
	}

	@Test
	void consultarProdutoPorCategoria() throws Exception {

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setCategoria(CategoriaProduto.LANCHE);
		produto.setPreco(BigDecimal.valueOf(15L));
		produto.setDescricao("x-salada");

		when(produtoService.listarPorCategoria(CategoriaProduto.LANCHE))
				.thenReturn(Stream.of(produto).collect(Collectors.toSet()));

		mockMvc.perform(get("/v1/produtos/" + CategoriaProduto.LANCHE.name())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.produtos[0].id").isNumber())
				.andExpect(MockMvcResultMatchers.jsonPath("$.produtos[0].categoria").value(produto.getCategoria().name()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.produtos[0].descricao").value(produto.getDescricao()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.produtos[0].preco").isNumber())
				.andReturn();
	}

	@Test
	void validarCacheExistente() throws Exception {

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setCategoria(CategoriaProduto.LANCHE);
		produto.setPreco(BigDecimal.valueOf(15L));
		produto.setDescricao("x-salada");

		redisTemplate.opsForValue().set(CategoriaProduto.LANCHE, Stream.of(produto).collect(Collectors.toSet()));

		mockMvc.perform(get("/v1/produtos/" + CategoriaProduto.LANCHE.name())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		verifyNoInteractions(produtoService);
	}

	@Test
	void excluirProduto() throws Exception{
		doAnswer(p -> CategoriaProduto.LANCHE).when(produtoService).excluir(1L);

		mockMvc.perform(delete("/v1/produtos/" + 1L)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
	}
}