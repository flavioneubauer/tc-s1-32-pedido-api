package br.com.fiap.soat1.t32.repositories.vendas;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;

public interface ClienteRepository  extends CrudRepository<Cliente, UUID> {

	public Cliente findByCpf(String cpf);
}