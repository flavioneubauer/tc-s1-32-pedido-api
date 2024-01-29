package br.com.fiap.soat1.t32.models.entities.pedidos;

import br.com.fiap.soat1.t32.enums.StatusPreparacaoPedido;
import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.EnumType.ORDINAL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PEDIDO")
public class Pedido {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column
    @Enumerated(ORDINAL)
    private StatusPreparacaoPedido statusPreparacao;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido")
    Set<PedidoProduto> produtos;

}
