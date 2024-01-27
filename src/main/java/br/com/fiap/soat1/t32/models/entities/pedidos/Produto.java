package br.com.fiap.soat1.t32.models.entities.pedidos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import br.com.fiap.soat1.t32.enums.CategoriaProduto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PRODUTO")
public class Produto {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProduto categoria;
}
