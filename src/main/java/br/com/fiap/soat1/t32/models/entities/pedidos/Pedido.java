package br.com.fiap.soat1.t32.models.entities.pedidos;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import br.com.fiap.soat1.t32.enums.StatusPagamentoPedido;
import br.com.fiap.soat1.t32.enums.StatusPreparacaoPedido;
import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;

import static jakarta.persistence.EnumType.STRING;

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
    @Enumerated(STRING)
    private StatusPreparacaoPedido statusPreparacao;

    @Column(nullable = false)
    @Enumerated(STRING)
    private StatusPagamentoPedido statusPagamento;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido")
    Set<PedidoProduto> produtos;

}
