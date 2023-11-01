# api-gerenciar-registro-vacinacao

API 3 Gerenciar Registro de Vacinação

This repository contains a simple CRUD project built using Java Spring. The aim of this repository is to practice and
share how you can build all CRUD Methods using Java Spring.

This project was build during a [live in my Youtube Channel](https://www.youtube.com/watch?v=tP6wtEaCnSI).

## Índice de Conteúdo

- [Instalação](#instalação)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Database](#database)

## Instalação

1. Clone the repository:

```bash
$ git clone https://github.com/mariojbe/api-gerenciar-registro-vacinacao.git
```

2. Install dependencies with Maven

## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080

## API Endpoints

The API provides the following endpoints:

```markdown
GET /product - Retrieve a list of all data.

POST /product - Register a new data.

PUT /product - Alter data.

DELETE /product/{id} - Inactivate data.
```

## Database

The project uses PostgresSQL as the database. The necessary database migrations are managed using Flyway.

To install PostgresSQL locally you can [click here](https://www.postgresql.org/download/).

## Docker

You can run this project with Docker by running the following command:

```bash
$ docker-compose up
```

Run the application and access http://localhost:15432

Enter the email and password configured in [Docker file](./docker-compose.yml).

To install Docker locally you can [click here](https://www.docker.com/products/docker-desktop/).
