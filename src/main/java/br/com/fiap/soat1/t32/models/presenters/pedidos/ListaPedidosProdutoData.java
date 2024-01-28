package br.com.fiap.soat1.t32.models.presenters.pedidos;

import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaPedidosProdutoData {

    private Long id;
    private String descricao;
    private CategoriaProduto categoria;
    private Long quantidade;

}
