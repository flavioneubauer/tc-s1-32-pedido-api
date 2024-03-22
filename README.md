# PEDIDO API

Serviço responsável pelos domínios de pedidos e vendas.
As principais funcionalidades são:

- Criação do pedido
- Alteração do status do pedido
- Consulta de pedidos
- Cadastro de clientes
- Consulta de clientes por cpf
- Cadastro de produtos
- CRUD de produtos

## Sequencia Saga

```mermaid
sequenceDiagram
    autonumber
    actor totem
    totem ->> Pagamento API: solicita checkout
    Pagamento API ->> DB Pagamento: persiste objeto pagamento
    DB Pagamento -->> Pagamento API: devolve objeto
    
    Pagamento API -->> totem: recebe o id do pagamento
    note over Pagamento API: aqui inicia a SAGA de criação do pedido

    Pagamento API ->> MQ: envia mensagem no topico novo-pedido<br>{"pagamentoId": 1, checkout: {}}
    MQ ->> Pedido API: consumer recebe a criação do pedido
    Pedido API ->> DB Pedido: persiste o novo pedido
    DB Pedido -->> Pedido API: devolve o objeto
    
    Pedido API -->> MQ: envia topico pedido-criado<br> com o id do pedido<br>{"pagamentoId": 1, pedidoId: 1}
    MQ -->> Pagamento API: consome topico pedido-criado
    
    Pagamento API ->> DB Pagamento: atualiza o pagamento<br> com o id do pedido

    Pagamento API ->> Meio Pagamento: processa pagamento
    note over Meio Pagamento: processo ficticio

    note over Pagamento API: aqui inicia a SAGA de recebimento do pagamento
    Meio Pagamento -->> Pagamento API: devolve no webhook
    Pagamento API ->> DB Pagamento: atualiza status do pagamento
    DB Pagamento -->> Pagamento API: recebe o objeto
    
    Pagamento API ->> MQ: publica no topico pagamento-pedido-pago<br>{"pedidoId": 1}
    MQ -->> Pedido API: consumer recebe atualização
    
    Pedido API ->> DB Pedido: atualiza status do pedido<br>cozinha pode preparar
```

Escolhemos o padrão **Coreografado** por ser uma sequência de interações entre apenas dois serviços, de forma que ambos podem ter suas ações e contra medidas de forma independente.
O padrão orquestrado implicaria em uma complexidade desnecessária para esse tipo de situação.

## Diagrama Arquitetural da comunicação entre os serviços

![diagrama-cloud](aws-infra.png)

![diagrama](tc-s1-32-entrega4-v6.drawio.png)

## ENTREGÁVEIS:

- [Relatório RIPD](relatorios/ripd/RIPD.pdf)
- [Relatório ZAP Scanning Report](relatorios/owasp_zap/ZAP%20Scanning%20Report%20-%20Fluxos%20solicitados.html)