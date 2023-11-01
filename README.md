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
- [Como Usar](#como-usar)
- [API Endpoints](#api-endpoints)
- [Base de Dados](#base-de-dados)

## Instalação

1. Clone o repositório:

```bash
$ git clone https://github.com/mariojbe/api-gerenciar-registro-vacinacao.git
```

2. Instale as dependências com o Maven

## Como Usar

1. Inicie o aplicativo com Maven
2. A API estará acessível em http://localhost:8080

## API Endpoints

A API fornece os seguintes endpoints:

```markdown
GET /api/vacinacao - Recuperar uma lista de todos os dados de vacinação.

GET /api/vacinacao/obter/{id} - Retorna dados de uma vacinação através do id.

POST /api/vacinacao/cadastrar - Registra uma nova vacinação.

PUT /api/vacinacao/editar/{id} - Atualiza dados da vacinação.

DELETE /api/vacinacao/remover/{id} - Excluir um registro de vacinação.

GET /api/vacinacao - Recuperar uma lista de todos os dados de vacinação.
```

## Base de Dados

O projeto usa MongoDB como banco de dados.

Para instalar o MongoDB localmente [clique aqui](https://www.mongodb.com/try/download/community).

OU

Experimente um cluster gratuito e altamente disponível de 512
MB. [clique aqui](https://www.mongodb.com/cloud/atlas/register).

