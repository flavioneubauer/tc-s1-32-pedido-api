Feature: the client api
  Scenario: user tries to add a new client
    Given a valid body request to register a new client
    When the user call POST /v1/clientes
    Then the client receives status code of 201
  Scenario: client send an invalid cpf
    Given an invalid cpf to register a new client
    When the user call POST /v1/clientes
    Then the client receives status code of 422
    And the request body has cpf invalido message