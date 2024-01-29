package br.com.fiap.soat1.t32.models.presenters.pedidos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaPedidosResponse {

	private List<ListaPedidosData> pedidos;
}
