package br.com.fiap.soat1.t32.services;

import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import br.com.fiap.soat1.t32.exceptions.ValidationException;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ProdutoVo;
import br.com.fiap.soat1.t32.models.presenters.pedidos.CriacaoProdutoResponse;
import br.com.fiap.soat1.t32.repositories.pedidos.ProdutoRepository;
import br.com.fiap.soat1.t32.utils.mappers.ProdutoAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ProdutoService {

	private final ProdutoRepository produtoRepository;

	public CriacaoProdutoResponse cadastrar(ProdutoVo produtoVo) {

		validarInclusaoAlteracao(produtoVo);

		return CriacaoProdutoResponse.builder().id(
				produtoRepository.save(ProdutoAdapter.toEntity(produtoVo, null)).getId())
				.build();
	}

	private void validarInclusaoAlteracao(ProdutoVo produto) {
		if(isNull(produto.getCategoria()) || isNull(produto.getDescricao()) ||
				(isNull(produto.getPreco()) || BigDecimal.ZERO.equals(produto.getPreco()))) {
			throw new ValidationException("Dados inválidos para alteração/inclusão de produto.");
		}
	}

	public CategoriaProduto excluir(Long produtoId) {
		var categoria = validarExclusao(produtoId);
		this.produtoRepository.deleteById(produtoId);
		return categoria;
	}

	private Produto getProduto(Long id){
		var produto = produtoRepository.findById(id);
		if(produto.isEmpty()) {
			throw new ValidationException("Produto não localizado.");
		}
		return produto.get();
	}

	private CategoriaProduto validarExclusao(Long id) {
		var produto = getProduto(id);
		return produto.getCategoria();
	}

	public void editar(Long idProduto, ProdutoVo produtoVo) {
		Produto produto = getProduto(idProduto);
		produto.setDescricao(produtoVo.getDescricao());
		produto.setCategoria(produtoVo.getCategoria());
		produto.setPreco(produtoVo.getPreco());
		this.produtoRepository.save(produto);
	}

	public Set<Produto> listarPorCategoria(CategoriaProduto categoriaProduto) {
		return ProdutoAdapter.toEntity(produtoRepository.findAllByCategoria(categoriaProduto));
	}
}
