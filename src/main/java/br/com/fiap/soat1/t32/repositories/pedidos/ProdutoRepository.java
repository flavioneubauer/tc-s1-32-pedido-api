package br.com.fiap.soat1.t32.repositories.pedidos;

import org.springframework.data.repository.CrudRepository;

import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;

import java.util.Set;

public interface ProdutoRepository extends CrudRepository<Produto, Long> {

    Set<Produto> findAllByCategoria(CategoriaProduto categoriaProduto);
}