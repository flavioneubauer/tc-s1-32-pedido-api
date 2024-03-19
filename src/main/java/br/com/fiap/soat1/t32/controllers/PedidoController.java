package br.com.fiap.soat1.t32.controllers;

import br.com.fiap.soat1.t32.enums.StatusPreparacaoPedido;
import br.com.fiap.soat1.t32.models.presenters.RespostaErro;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ListaPedidosResponse;
import br.com.fiap.soat1.t32.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pedido", description = "API de Pedido")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
class PedidoController {

	private final PedidoService pedidoService;

	@PutMapping("/v1/pedidos/{id}/{status}")
	@ApiResponse(responseCode = "204", description = "Status de preparação do pedido alterado com sucesso")
	@ApiResponse(responseCode = "422", description = "Erro de validação",
			content = @Content(schema = @Schema(implementation = RespostaErro.class)))
	@Operation(description = "Altera status de preparação do pedido")
	public ResponseEntity<Void> alterarStatusPreparacaoPedido(@PathVariable Long id, @PathVariable StatusPreparacaoPedido status) {
		pedidoService.alterarStatusPreparacaoPedido(id, status);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/v1/pedidos")
	@ApiResponse(responseCode = "200", description = "Status de pedido retornado com sucesso")
	@Operation(description = "Lista pedidos")
	public ResponseEntity<ListaPedidosResponse> listarPedidos() {
		return ResponseEntity.ok(pedidoService.listar());
	}
}