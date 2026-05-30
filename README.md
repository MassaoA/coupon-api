# Coupon API

API REST para gerenciamento de cupons, desenvolvida com Java 21 e Spring Boot 4.

## Tecnologias

- Java 21
- Spring Boot 4
- Spring Data JPA
- H2 (banco de dados em memória)
- Lombok
- SpringDoc OpenAPI (Swagger)
- Docker & Docker Compose
- JUnit 5 + Mockito

## Arquitetura

O projeto segue os princípios da **Clean Architecture**, organizado em quatro camadas:

```
src/main/java/com/marcioaraki/coupon_api
├── domain          # Entidades, interfaces de casos de uso, interfaces de repositório, exceções
├── application     # Implementações dos casos de uso (services)
├── infrastructure  # Entidades JPA, repositórios Spring Data
└── api             # Controllers, DTOs, handler de exceções
```

Todas as regras de negócio estão encapsuladas na entidade de domínio `Coupon`.

## Regras de Negócio

- O código do cupom é alfanumérico com exatamente 6 caracteres — caracteres especiais são removidos automaticamente antes de salvar
- Valor mínimo de desconto: 0,5
- A data de expiração não pode estar no passado
- Um cupom pode ser criado já publicado
- O delete é um soft delete — o registro é mantido no banco com o status `DELETED`
- Um cupom já deletado não pode ser deletado novamente

## Endpoints

| Método | Path | Descrição |
|--------|------|-----------|
| POST | `/coupon` | Criar um novo cupom |
| GET | `/coupon/{id}` | Buscar cupom por ID |
| DELETE | `/coupon/{id}` | Soft delete de um cupom |

## Executando com Docker

```bash
docker-compose up --build
```

## Executando localmente

```bash
./gradlew bootRun
```

## Executando os testes

```bash
./gradlew test
```

## Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:coupondb`
  - Usuário: `sa`
  - Senha: *(vazia)*
