package br.com.fiap.soat1.t32.controllers;

import br.com.fiap.soat1.t32.models.parameters.vendas.ClienteVO;
import br.com.fiap.soat1.t32.models.presenters.RespostaErro;
import br.com.fiap.soat1.t32.models.presenters.vendas.ConsultaClienteResponse;
import br.com.fiap.soat1.t32.services.vendas.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Checkout", description = "API Clientes")
@RequestMapping(value = "/v1/clientes",
        consumes = {APPLICATION_JSON_VALUE, ALL_VALUE},
        produces = {APPLICATION_JSON_VALUE})
@RestController
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(description = "Inclui cliente")
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, ALL_VALUE},
            produces = {ALL_VALUE})
	@ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso")
	@ApiResponse(responseCode = "422", description = "Erro de validação",
			content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    public ResponseEntity<Void> cadastrarCliente(@RequestBody @Valid ClienteVO clienteVO) {
		clienteService.cadastrar(clienteVO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @Operation(description = "Exclui cliente")
    @DeleteMapping(consumes = {APPLICATION_JSON_VALUE, ALL_VALUE},
            produces = {ALL_VALUE})
	@ApiResponse(responseCode = "200", description = "Cliente excluido com sucesso")
	@ApiResponse(responseCode = "422", description = "Erro de validação",
			content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    public ResponseEntity<Void> excluirCliente(@RequestBody @Valid ClienteVO clienteVO) throws Exception {
		clienteService.ecluirCliente(clienteVO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
	
	@Operation(description = "Consultar cliente por CPF")
	@GetMapping(path = "/{cpf}", 
		consumes = { ALL_VALUE }, 
		produces = { APPLICATION_JSON_VALUE, ALL_VALUE })
	@ApiResponse(responseCode = "200", description = "Clientes listados com sucesso")
	@ApiResponse(responseCode = "422", description = "Erro de validação",
			content = @Content(schema = @Schema(implementation = RespostaErro.class)))
	@SecurityRequirement(name = "Authorization")
	public ResponseEntity<ConsultaClienteResponse> consultarClientePorCpf(@PathVariable String cpf) {
		return ResponseEntity.ok(clienteService.consultarPorCpf(cpf));
	}
}