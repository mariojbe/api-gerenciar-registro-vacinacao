# api-gerenciar-registro-vacinacao

API 3 Gerenciar Registro de Vacinação

Este repositório contém um projeto API REST simples construído usando Java Spring & MongoDB.
O objetivo deste repositório é gerenciar o registro de vacinação através de CRUD Java com o seguintes dados:

• Identificação do Paciente
• Identificação da Vacina
• Identificação da Dose
• Identificação do Profissional de Saúde (Nome e CPF)

Este projeto foi construído durante a disciplina de Programação para Web II
do curso de Bacharelado em Sitemas de Informação da UNIME(União Metropolitana de Educação e Cultura)
no 2º semestre de 2023.

## Índice de Conteúdo

- [Instalação](#instalação)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Base de Dados](#base-de-dados)

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

## Base de Dados

O projeto usa MongoDB como banco de dados.

Para instalar o MongoDB localmente [clique aqui](https://www.mongodb.com/try/download/community).

Experimente um cluster gratuito e altamente disponível de 512
MB.[clique aqui](https://www.mongodb.com/cloud/atlas/register).

