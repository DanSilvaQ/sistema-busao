# Sistema de Gerenciamento de Transporte Rodoviário (Microservice API - v1)

## 🚌 Descrição do Projeto

Este projeto implementa uma API RESTful para o gerenciamento de recursos essenciais em uma empresa de transporte rodoviário (Motoristas, Ônibus e Viagens). Foi desenvolvido utilizando **Java** e o *framework* **Quarkus**, focado em alta performance e arquitetura de microserviços.

### Funcionalidades e Requisitos Avançados Implementados:

1.  **Versionamento de API (V1):** Todos os *endpoints* utilizam o prefixo `/api/v1/...`.
2.  **Idempotência:** A criação de recursos (`POST`) é protegida pelo cabeçalho `Idempotency-Key` para prevenir duplicação de requisições.
3.  **Validação de Dados:** Utilização de Bean Validation (`@Valid`, `@NotNull`, `@Pattern`) com tratamento de erro HTTP 400 (Bad Request).
4.  **Tratamento de Relacionamentos:** Checagem da existência de chaves estrangeiras (`Motorista` e `Ônibus`) com retorno HTTP 404 (Not Found).
5.  **Regra de Negócio Crucial (Viagem):** A API impede a criação de uma viagem se o `Motorista.tipoHabilitacaoOnibus` for incompatível com o `Onibus.tipoOnibus`.
6.  **Filtros de Busca Específicos:** Implementação de *endpoints* `/search` para buscas detalhadas.

---

## 🛠️ Instruções Detalhadas para Execução

### Pré-requisitos

* **Java 17+** (JDK)
* **Maven** (3.8+)
* **Docker** (Para rodar o banco de dados PostgreSQL)

### 1. Inicialização do Banco de Dados (PostgreSQL via Docker)

Recomendamos usar o Docker para iniciar rapidamente uma instância de banco de dados.

```bash
docker run --name busao-db -e POSTGRES_USER=appuser -e POSTGRES_PASSWORD=apppass -e POSTGRES_DB=busaodb -p 5432:5432 -d postgres:15