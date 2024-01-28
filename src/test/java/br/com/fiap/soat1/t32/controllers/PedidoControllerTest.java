package br.com.fiap.soat1.t32.controllers;

import br.com.fiap.soat1.t32.TestHelper;
import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import br.com.fiap.soat1.t32.enums.StatusPreparacaoPedido;
import br.com.fiap.soat1.t32.exceptions.ValidationException;
import br.com.fiap.soat1.t32.models.parameters.pedidos.PedidoVo;
import br.com.fiap.soat1.t32.models.presenters.pedidos.*;
import br.com.fiap.soat1.t32.services.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.UUID;

import static br.com.fiap.soat1.t32.TestHelper.asJsonString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PedidoService pedidoService;

	private PedidoVo pedidoVo;

	@BeforeEach
	public void setup() {
		pedidoVo = new PedidoVo();
	}

	@Test
	void testarAdicionarPedidoComSucesso() throws Exception {
		when(pedidoService.cadastrar(pedidoVo)).thenReturn(CriacaoPedidoResponse.builder()
				.id(1L)
				.build());

		mockMvc.perform(post("/v1/pedidos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(pedidoVo)))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andReturn();
	}

	@Test
	void testarPedidoSemProduto() throws Exception {
		when(pedidoService.cadastrar(pedidoVo)).thenThrow(new ValidationException("Produto não encontrado"));

		mockMvc.perform(post("/v1/pedidos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(pedidoVo)))
				.andExpect(status().is4xxClientError())
				.andReturn();
	}

	@Test
	void testarAlterarStatusPreparacaoPedidoComSucesso() throws Exception {
		mockMvc.perform(put("/v1/pedidos/1/" + StatusPreparacaoPedido.EM_PREPARACAO.name())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
	}

	@Test
	void testarAlterarStatusPreparacaoPedidoInvalido() throws Exception {
		doThrow(new ValidationException("Status invalido")).when(pedidoService)
				.alterarStatusPreparacaoPedido(1L, StatusPreparacaoPedido.FINALIZADO);

		mockMvc.perform(put("/v1/pedidos/1/" + StatusPreparacaoPedido.FINALIZADO.name())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andReturn();
	}

	@Test
	void listarPedidos() throws Exception {

		ListaPedidosResponse listaPedidosResponse = ListaPedidosResponse.builder()
				.pedidos(Arrays.asList(
						ListaPedidosData.builder()
								.produtos(Arrays.asList(ListaPedidosProdutoData.builder().
										id(1L).categoria(CategoriaProduto.LANCHE)
												.descricao("teste").quantidade(2L)
										.build()))
								.cliente(ListaPedidosClienteData.builder().id(UUID.randomUUID()).nome("Jose").build())
								.statusPreparacao(StatusPreparacaoPedido.EM_PREPARACAO)
						.build()
				))
				.build();

		when(pedidoService.listar()).thenReturn(listaPedidosResponse);

		mockMvc.perform(get("/v1/pedidos")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().json(TestHelper.asJsonString(listaPedidosResponse)))
				.andReturn();
	}
}