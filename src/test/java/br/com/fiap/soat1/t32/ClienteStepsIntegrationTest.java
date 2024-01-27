package br.com.fiap.soat1.t32;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpStatusCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class ClienteStepsIntegrationTest extends SpringIntegrationTest {

    private String body;

    @Given("a valid body request to register a new client")
    public void a_valid_body_request_to_register_a_new_client() {
       this.body = "{\"nome\":\"João da Silva\",\"cpf\":\"41765533082\",\"email\":\"joao@example.com\"}";
    }

    @Given("an invalid cpf to register a new client")
    public void anInvalidCpfToRegisterANewClient() {
        this.body = "{\"nome\":\"João da Silva\",\"cpf\":\"12345678900\",\"email\":\"joao@example.com\"}";
    }

    @When("the user call POST \\/v1\\/clientes")
    public void theUserCallPOSTVClientes() throws IOException{
        executePost("http://localhost:8080/v1/clientes",body);
    }
    
    @Then("the client receives status code of {int}")
    public void the_client_receives_status_code_of(Integer statusCode) throws IOException {
        final HttpStatusCode currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        assertThat("status code is: " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @And("the request body has cpf invalido message")
    public void theRequestBodyHasCpfInvalidoMessage() {
        assertTrue(latestResponse.getBody().contains("CPF informado inválido"));
    }



}
