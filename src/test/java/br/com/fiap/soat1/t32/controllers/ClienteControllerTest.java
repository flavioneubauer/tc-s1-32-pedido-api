package br.com.fiap.soat1.t32.controllers;

import br.com.fiap.soat1.t32.models.parameters.vendas.ClienteVO;
import br.com.fiap.soat1.t32.models.presenters.vendas.ConsultaClienteResponse;
import br.com.fiap.soat1.t32.services.vendas.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static br.com.fiap.soat1.t32.TestHelper.asJsonString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class) class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private ClienteVO clienteVO;

    @BeforeEach
    public void setup() {
        clienteController = new ClienteController(clienteService);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();

        clienteVO = new ClienteVO();
        clienteVO.setCpf("12345678900");
        clienteVO.setEmail("jose@example.com");
        clienteVO.setNome("José");
    }

    @Test
    void testCadastrarCliente() throws Exception {
        mockMvc.perform(post("/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteVO)))
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
        verify(clienteService).cadastrar(clienteVO);
    }

    @Test
    void testConsultarClientePorCpf() throws Exception {
        // Arrange
        String cpf = "123456789";
        ConsultaClienteResponse expectedResponse = new ConsultaClienteResponse();
        when(clienteService.consultarPorCpf(cpf)).thenReturn(expectedResponse);

        mockMvc.perform(get("/v1/clientes/{cpf}", cpf))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(expectedResponse)));

        // Assert
        verify(clienteService).consultarPorCpf(cpf);
    }
}