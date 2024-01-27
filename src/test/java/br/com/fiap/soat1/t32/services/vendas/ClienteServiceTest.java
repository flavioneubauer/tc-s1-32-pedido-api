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
import org.mockito.Mockito;

import br.com.fiap.soat1.t32.exceptions.NotFoundException;
import br.com.fiap.soat1.t32.exceptions.ValidationException;
import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;
import br.com.fiap.soat1.t32.models.parameters.vendas.ClienteVO;
import br.com.fiap.soat1.t32.models.presenters.vendas.ConsultaClienteResponse;
import br.com.fiap.soat1.t32.repositories.vendas.ClienteRepository;


class ClienteServiceTest {

    private ClienteRepository clienteRepository;
    private CpfValidatorService cpfValidatorService;
    private EmailValidatorService emailValidatorService;
    private ClienteService clienteService;

    private ClienteVO clienteVO;

    @BeforeEach
    void setUp() {
        clienteRepository = mock(ClienteRepository.class);
        cpfValidatorService = mock(CpfValidatorService.class);
        emailValidatorService = mock(EmailValidatorService.class);
        clienteService = new ClienteService(clienteRepository, cpfValidatorService, emailValidatorService);

        clienteVO = new ClienteVO();
        clienteVO.setCpf("12345678900");
        clienteVO.setEmail("jose@example.com");
        clienteVO.setNome("José");
    }

    @Test
    void cadastrar_ValidClienteVO_CallsRepositorySave() {
        // Set up the mock behavior
        when(cpfValidatorService.isValido(clienteVO.getCpf())).thenReturn(true);
        when(emailValidatorService.isValido(clienteVO.getEmail())).thenReturn(true);

        clienteService.cadastrar(clienteVO);

        // Verify that the repository's save method was called
        verify(clienteRepository).save(Mockito.any());
    }
    
    @Test
    void cadastrar_InvalidCpf_ThrowsValidationException() {
        ClienteVO clienteVO = new ClienteVO();
        // Set up the mock behavior
        when(cpfValidatorService.isValido(clienteVO.getCpf())).thenReturn(false);

        // Verify that a ValidationException is thrown
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

        // Verify that the response is not null
        assertNotNull(response);
        // Verify that the response contains the expected cliente
        assertEquals(cliente.getId(), response.getId());
    }

    @Test
    void consultarPorCpf_NonExistingCpf_ThrowsNotFoundException() {
        String cpf = "12345678900";
        // Set up the mock behavior
        when(clienteRepository.findByCpf(cpf)).thenReturn(null);

        // Verify that a NotFoundException is thrown
        assertThrows(NotFoundException.class, () -> clienteService.consultarPorCpf(cpf));
    }

}
