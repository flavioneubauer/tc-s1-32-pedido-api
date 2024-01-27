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
  Scenario: client send an invalid email
    Given an invalid email to register a new client
    When the user call POST /v1/clientes
    Then the client receives status code of 422
    And the request body has email invalido message
  Scenario: client send an invalid name
    Given an invalid name to register a new client
    When the user call POST /v1/clientes
    Then the client receives status code of 422
    And the request body has nome invalido message
  Scenario: client get object by cpf
    Given an existing cpf in the database
    When the user call GET /v1/clientes/cpf
    Then the client receives status code of 200
    And the request body has client data