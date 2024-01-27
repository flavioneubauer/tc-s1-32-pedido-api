package br.com.fiap.soat1.t32.models.entities.vendas;

import lombok.*;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy= GenerationType.UUID)
	private UUID id;
	private String nome;
	private String cpf;
	private String email;
}
