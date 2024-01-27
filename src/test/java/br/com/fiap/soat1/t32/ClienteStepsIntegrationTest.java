package br.com.fiap.soat1.t32;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class ClienteStepsIntegrationTest extends SpringIntegrationTest {

	private String body;
	private String cpf;

	@Given("a valid body request to register a new client")
	public void a_valid_body_request_to_register_a_new_client() {
		this.body = "{\"nome\":\"João da Silva\",\"cpf\":\"41765533082\",\"email\":\"joao@example.com\"}";
	}

	@Given("an invalid cpf to register a new client")
	public void anInvalidCpfToRegisterANewClient() {
		this.body = "{\"nome\":\"João da Silva\",\"cpf\":\"12345678900\",\"email\":\"joao@example.com\"}";
	}

	@Given("an invalid name to register a new client")
	public void anInvalidNameToRegisterANewClient() {
		this.body = "{\"nome\":\"\",\"cpf\":\"41765533082\",\"email\":\"joao@example.com\"}";
	}

	@Given("an invalid email to register a new client")
	public void anInvalidEmailToRegisterANewClient() {
		this.body = "{\"nome\":\"João da Silva\",\"cpf\":\"41765533082\",\"email\":\"@example.com\"}";
	}

	@Given("an existing cpf in the database")
	public void anExistingCpfInTheDatabase() {
		this.cpf = "41765533082";
	}

	@When("the user call POST \\/v1\\/clientes")
	public void theUserCallPOSTVClientes() throws IOException {
		executePost("http://localhost:8080/v1/clientes", body);
	}

	@When("the user call GET \\/v1\\/clientes\\/cpf")
	public void theUserCallGETVClientesCpf() throws IOException {
		executeGet("http://localhost:8080/v1/clientes/" + cpf);
	}

	@Then("the client receives status code of {int}")
	public void the_client_receives_status_code_of(Integer statusCode) throws IOException {
		final HttpStatusCode currentStatusCode = latestResponse.getTheResponse()
				.getStatusCode();
		assertThat("status code is: " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
	}

	@And("the request body has cpf invalido message")
	public void theRequestBodyHasCpfInvalidoMessage() {
		assertTrue(latestResponse.getBody()
				.contains("CPF informado inválido"));
	}

	@And("the request body has email invalido message")
	public void theRequestBodyHasEmailInvalidoMessage() {
		assertTrue(latestResponse.getBody()
				.contains("E-mail informado inválido"));
	}

	@And("the request body has nome invalido message")
	public void theRequestBodyHasNomeInvalidoMessage() {
		assertTrue(latestResponse.getBody()
				.contains("Nome informado inválido"));
	}

	@And("the request body has client data")
	public void theRequestBodyHasClientData() {
		assertTrue(latestResponse.getBody().contains(this.cpf));
	}
}
