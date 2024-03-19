package br.com.fiap.soat1.t32.services;

import br.com.fiap.soat1.t32.enums.StatusPreparacaoPedido;
import br.com.fiap.soat1.t32.models.presenters.pedidos.CriacaoPedidoRequest;
import br.com.fiap.soat1.t32.models.presenters.pedidos.CriacaoPedidoResponse;
import br.com.fiap.soat1.t32.models.presenters.pedidos.PagamentoPedidoAutorizado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoEventListenerService {

	private final PedidoService pedidoService;
	private final RabbitTemplate rabbitTemplate;

	@Value("${fila.pedido-criado.nome}")
	private String filaPedidoCriado;

	@Value("${fila.falha-criacao-pedido.nome}")
	private String filaFalhaCriarPedido;


	@RabbitListener(queues = {"${fila.pedidos.nome}"})
	public void cadastrar(CriacaoPedidoRequest criacaoPedidoRequest) {
		try{
			CriacaoPedidoResponse criacaoPedidoResponse = pedidoService.cadastrar(criacaoPedidoRequest);

			rabbitTemplate.convertAndSend(filaPedidoCriado, criacaoPedidoResponse);
		}catch (Exception ex){
			log.error("Falha ao criar pedido", ex);
			rabbitTemplate.convertAndSend(filaFalhaCriarPedido, criacaoPedidoRequest);
		}
	}

	@RabbitListener(queues = {"${fila.pagamento-pedido-autorizado.nome}"})
	public void alterarStatusPedidoPagamentoAutorizado(PagamentoPedidoAutorizado pagamentoPedidoAutorizado) {
		pedidoService.alterarStatusPreparacaoPedido(pagamentoPedidoAutorizado.getIdPedido(), StatusPreparacaoPedido.RECEBIDO);
	}
}
