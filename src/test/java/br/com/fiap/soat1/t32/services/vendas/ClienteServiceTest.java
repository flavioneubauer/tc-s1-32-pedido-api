package br.com.fiap.soat1.t32.services.vendas;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.com.fiap.soat1.t32.exceptions.NotFoundException;
import br.com.fiap.soat1.t32.exceptions.ValidationException;
import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;
import br.com.fiap.soat1.t32.models.parameters.vendas.ClienteVO;
import br.com.fiap.soat1.t32.models.presenters.vendas.ConsultaClienteResponse;
import br.com.fiap.soat1.t32.repositories.vendas.ClienteRepository;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private CpfValidatorService cpfValidatorService;
    @Mock
    private EmailValidatorService emailValidatorService;
    @InjectMocks
    private ClienteService clienteService;

    private ClienteVO clienteVO;

    @BeforeEach
    void setUp() {
        clienteVO = new ClienteVO();
        clienteVO.setCpf("12345678900");
        clienteVO.setEmail("jose@example.com");
        clienteVO.setNome("José");
    }

    @Test
    void cadastrar_ValidClienteVO_CallsRepositorySave() {
        when(cpfValidatorService.isValido(clienteVO.getCpf())).thenReturn(true);
        when(emailValidatorService.isValido(clienteVO.getEmail())).thenReturn(true);

        clienteService.cadastrar(clienteVO);

        verify(clienteRepository).save(Mockito.any());
    }
    
    @Test
    void cadastrar_InvalidCpf_ThrowsValidationException() {
        when(cpfValidatorService.isValido(clienteVO.getCpf())).thenReturn(false);

        assertThrows(ValidationException.class, () -> clienteService.cadastrar(clienteVO));
    }

    @Test
    void consultarPorCpf_ExistingCpf_ReturnsConsultaClienteResponse() {
        String cpf = "12345678900";
        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);
        cliente.setId(UUID.randomUUID());
        // Set up the mock behavior
        when(clienteRepository.findByCpf(cpf)).thenReturn(cliente);

        ConsultaClienteResponse response = clienteService.consultarPorCpf(cpf);

        assertNotNull(response);
        assertEquals(cliente.getId(), response.getId());
    }

    @Test
    void consultarPorCpf_NonExistingCpf_ThrowsNotFoundException() {
        String cpf = "12345678900";
        when(clienteRepository.findByCpf(cpf)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> clienteService.consultarPorCpf(cpf));
    }

}
