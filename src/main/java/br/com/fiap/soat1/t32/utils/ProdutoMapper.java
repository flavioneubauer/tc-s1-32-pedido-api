package br.com.fiap.soat1.t32.utils;

import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ProdutoVo;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ConsultaProdutoData;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ConsultaProdutoResponse;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ProdutoMapper {

    public static Produto toEntity(ProdutoVo produto, Long produtoId) {
        return Produto.builder()
                .id(produtoId)
                .categoria(produto.getCategoria())
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .build();
    }

    public static Set<Produto> toEntity(Set<Produto> produtos) {
        return produtos == null ? null : produtos.stream()
                .map(ProdutoMapper::toEntity)
                .collect(Collectors.toSet());
    }

    public static Produto toEntity(Produto produto) {
        return Produto.builder()
                .categoria(produto.getCategoria())
                .descricao(produto.getDescricao())
                .id(produto.getId())
                .preco(produto.getPreco())
                .build();
    }

    public static ConsultaProdutoResponse toResponse(Set<Produto> produtos) {

        return ConsultaProdutoResponse.builder()
                .produtos(produtos.stream()
                        .map(produto -> ConsultaProdutoData.builder()
                                .categoria(produto.getCategoria())
                                .descricao(produto.getDescricao())
                                .id(produto.getId())
                                .preco(produto.getPreco())
                                .build())
                        .toList())
                .build();
    }

}
