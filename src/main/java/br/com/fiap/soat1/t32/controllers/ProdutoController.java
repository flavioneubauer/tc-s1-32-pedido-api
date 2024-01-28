package br.com.fiap.soat1.t32.controllers;

import br.com.fiap.soat1.t32.enums.CategoriaProduto;
import br.com.fiap.soat1.t32.models.entities.pedidos.Produto;
import br.com.fiap.soat1.t32.models.parameters.pedidos.ProdutoVo;
import br.com.fiap.soat1.t32.models.presenters.RespostaErro;
import br.com.fiap.soat1.t32.models.presenters.pedidos.ConsultaProdutoResponse;
import br.com.fiap.soat1.t32.models.presenters.pedidos.CriacaoProdutoResponse;
import br.com.fiap.soat1.t32.services.ProdutoService;
import br.com.fiap.soat1.t32.utils.mappers.ProdutoAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Produto", description = "API de Produtos")
@RequestMapping(value = "/v1/produtos",
        consumes = {APPLICATION_JSON_VALUE, ALL_VALUE},
        produces = {APPLICATION_JSON_VALUE})
@RestController
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;
    private final RedisTemplate<CategoriaProduto, Set<Produto>> redisTemplate;
    @Operation(description = "Inclui produto")
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, ALL_VALUE},
            produces = {APPLICATION_JSON_VALUE, ALL_VALUE})
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
    @ApiResponse(responseCode = "422", description = "Erro de validação",
            content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<CriacaoProdutoResponse> criarProduto(@RequestBody ProdutoVo produtoVo) {
        CriacaoProdutoResponse produtoCadastrado = produtoService.cadastrar(produtoVo);
        invalidateCache(produtoVo.getCategoria());
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCadastrado);
    }

    @Operation(description = "Exclui produto")
    @DeleteMapping(path = "/{produtoId}",
            consumes = {APPLICATION_JSON_VALUE, ALL_VALUE},
            produces = {ALL_VALUE})
    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso")
    @ApiResponse(responseCode = "422", description = "Erro de validação",
            content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long produtoId) {
        CategoriaProduto categoriaProdutoExcluido = produtoService.excluir(produtoId);
        invalidateCache(categoriaProdutoExcluido);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Editar produto")
    @PutMapping(path = "/{produtoId}",
            consumes = {APPLICATION_JSON_VALUE, ALL_VALUE},
            produces = {ALL_VALUE})
    @ApiResponse(responseCode = "204", description = "Produto alterado com sucesso")
    @ApiResponse(responseCode = "422", description = "Erro de validação",
            content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Void> editarProduto(@PathVariable Long produtoId,
                                              @RequestBody ProdutoVo produtoVo) {
        produtoService.editar(produtoId, produtoVo);
        invalidateCache(produtoVo.getCategoria());
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "Consultar produto por categoria")
    @GetMapping(path = "/{categoriaProduto}",
            consumes = {ALL_VALUE},
            produces = {APPLICATION_JSON_VALUE, ALL_VALUE})
    @ApiResponse(responseCode = "200", description = "Lista de produtos por categoria retornada com sucesso")
    @ApiResponse(responseCode = "422", description = "Erro de validação",
            content = @Content(schema = @Schema(implementation = RespostaErro.class)))
    public ResponseEntity<ConsultaProdutoResponse> consultarProdutoPorCategoria(@PathVariable CategoriaProduto categoriaProduto) {
        Set<Produto> produtos = redisTemplate.opsForValue()
                .get(categoriaProduto);
        if(produtos == null || produtos.isEmpty()){
            produtos = produtoService.listarPorCategoria(categoriaProduto);
            adicionarCache(categoriaProduto, produtos);
        }
        return ResponseEntity.ok(ProdutoAdapter.toResponse(produtos));
    }

    private void invalidateCache(CategoriaProduto categoriaProduto){
        redisTemplate.opsForValue().set(categoriaProduto, new HashSet<>());
    }

    private void adicionarCache(CategoriaProduto categoriaProduto, Set<Produto> produtos){
        redisTemplate.opsForValue().set(categoriaProduto, produtos, Duration.ofMinutes(60));
    }
}
