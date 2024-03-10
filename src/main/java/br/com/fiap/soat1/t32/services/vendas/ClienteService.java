package br.com.fiap.soat1.t32.services.vendas;

import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.soat1.t32.exceptions.DuplicateKeyException;
import br.com.fiap.soat1.t32.exceptions.NotFoundException;
import br.com.fiap.soat1.t32.exceptions.ValidationException;
import br.com.fiap.soat1.t32.models.entities.vendas.Cliente;
import br.com.fiap.soat1.t32.models.parameters.vendas.ClienteVO;
import br.com.fiap.soat1.t32.models.presenters.vendas.ConsultaClienteResponse;
import br.com.fiap.soat1.t32.repositories.vendas.ClienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

	private final ClienteRepository clienteRepository;
	private final CpfValidatorService cpfValidatorService;
	private final EmailValidatorService emailValidatorService;

	@Transactional
	public void cadastrar(ClienteVO clienteVO) {
		validar(clienteVO);
		Cliente cliente = clienteVO.toCliente();
		try {
			clienteRepository.save(cliente);
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateKeyException("CPF informado já cadastrado!");
		}
	}
	
	@Transactional
	public void ecluirCliente(ClienteVO clienteVO) throws Exception {
		Cliente cliente = clienteRepository.findByCpf(clienteVO.getCpf());
		
		if(Objects.isNull(cliente)) {
			cliente = clienteRepository.findByNome(clienteVO.getNome());
		}
		
		if(Objects.isNull(cliente)) {
			cliente = Optional.ofNullable(clienteRepository.findByEmail(clienteVO.getEmail()))
					.orElseThrow(() -> new NotFoundException("Cliente não cadastrado."));
		}
				
		try {
			clienteRepository.delete(cliente);
		} catch (Exception e) {
			throw new Exception("Erro ao excluir cliente!");
		}
	}

	@Transactional(readOnly = true)
	public ConsultaClienteResponse consultarPorCpf(String cpf) {
		Cliente cliente = Optional.ofNullable(clienteRepository.findByCpf(cpf))
			.orElseThrow(() -> new NotFoundException("Cliente não cadastrado."));
		
		return ConsultaClienteResponse.builder()
				.id(cliente.getId())
				.nome(cliente.getNome())
				.cpf(cliente.getCpf().replace(".", "").replace("-", ""))
				.email(cliente.getEmail())
				.build();
	}

	private void validar(ClienteVO clienteVO) {

		if(clienteVO == null){
			throw new ValidationException("Cliente não informado.");
		}

		if (!cpfValidatorService.isValido(clienteVO.getCpf())) {
			throw new ValidationException("CPF informado inválido.");
		}

		if (!emailValidatorService.isValido(clienteVO.getEmail())) {
			throw new ValidationException("E-mail informado inválido.");
		}

		if (clienteVO.getNome() == null || clienteVO.getNome().trim().isEmpty()) {
			throw new ValidationException("Nome informado inválido.");
		}
	}
}
