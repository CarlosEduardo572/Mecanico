# 🔧 Mecânico: Sistema de Oficina em Microsserviços

Trabalho 4 de Arquitetura de Sistemas: uma oficina mecânica dividida em **quatro microsserviços** independentes (Python e Java), cada um com o **seu próprio banco de dados**, conectados por um **front-end** em HTML e JavaScript e publicados na **nuvem**.

[![Site](https://img.shields.io/badge/site-online-1565C0?style=for-the-badge)](https://oficina-frontend-sc9n.onrender.com)
[![Vídeo](https://img.shields.io/badge/apresentação-YouTube-C62828?style=for-the-badge)](https://youtu.be/qtWOPADaR4w)

![Python](https://img.shields.io/badge/Python-3.12-3776AB?logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-009688?logo=fastapi&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Deploy-Render-46E3B7?logo=render&logoColor=white)

---

## 👥 Equipe

| Integrante | GitHub | Linguagem | Microsserviços |
|---|---|---|---|
| **Carlos Eduardo Fernandes Ribeiro Rodrigues** | [@CarlosEduardo572](https://github.com/CarlosEduardo572) | Python (FastAPI) | Ordens de Serviço · Notificações |
| **Letícia Fermanian** | [@Fermanian21](https://github.com/Fermanian21) | Java (Spring Boot) | Cliente e Veículos · Catálogo e Estoque |

---

## 🌐 Aplicação na nuvem

| Componente | Link |
|---|---|
| 🖥️ **Front-end** | https://oficina-frontend-sc9n.onrender.com |
| 📋 Ordens de Serviço | https://servico-os.onrender.com/ordens |
| 🔔 Notificações | https://notificacoes-5vhb.onrender.com/notificacoes |
| 👤 Cliente e Veículos | https://clientes-veiculos.onrender.com/clientes |
| 📦 Catálogo e Estoque | https://catalogo-estoque.onrender.com/pecas |

> ⏳ **Aviso:** no plano gratuito da Render os serviços "dormem" após cerca de 15 minutos sem uso. O primeiro acesso pode levar cerca de 1 minuto (os serviços Java demoram um pouco mais). Abra as URLs acima antes de testar.

---

## 🏗️ Arquitetura

```mermaid
flowchart TD
    U([👤 Usuário]) --> F["🖥️ Front-end<br/>HTML + JavaScript"]

    F --> OS["📋 Ordens de Serviço<br/>Python / FastAPI<br/><i>Carlos</i>"]
    F --> CV["👤 Cliente e Veículos<br/>Java / Spring Boot<br/><i>Letícia</i>"]
    F --> NT["🔔 Notificações<br/>Python / FastAPI<br/><i>Carlos</i>"]
    F --> CE["📦 Catálogo e Estoque<br/>Java / Spring Boot<br/><i>Letícia</i>"]

    OS -. "GET /clientes/{id}" .-> CV
    OS -. "PATCH /pecas/{id}/estoque" .-> CE
    OS -. "POST /notificacoes" .-> NT

    OS --- D1[("🗄️ Neon<br/>PostgreSQL")]
    CV --- D2[("🗄️ Neon<br/>PostgreSQL")]
    NT --- D3[("🗄️ Neon<br/>PostgreSQL")]
    CE --- D4[("🗄️ Neon<br/>PostgreSQL")]
```

As setas tracejadas mostram a comunicação **entre microsserviços**: o serviço de Ordens de Serviço atua como **consumidor** dos serviços de Clientes, Catálogo e Notificações. Toda a troca de dados é feita por **HTTP + JSON**.

---

## 📁 Estrutura do repositório

```
Mecanico/
├── servico-os/          # 📋 Ordens de Serviço       (Python / FastAPI)  - Carlos
├── notificacoes/        # 🔔 Notificações            (Python / FastAPI)  - Carlos
├── clientes-veiculos/   # 👤 Cliente e Veículos      (Java / Spring)     - Letícia
├── catalogo-estoque/    # 📦 Catálogo e Estoque      (Java / Spring)     - Letícia
├── frontend/            # 🖥️ Página única que conecta os quatro serviços
└── legado-trabalho3/    # 🗂️ Código do trabalho anterior (monolito, mantido como histórico)
```

---

## 📋 Serviço de Ordens de Serviço (`servico-os`)

Cuida do ciclo de vida de uma ordem de serviço. Antes de abrir uma ordem, **valida o cliente** no serviço de Clientes; ao usar uma peça, **baixa o estoque** no Catálogo; e a cada evento **envia uma notificação**.

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/ordens` | Lista as ordens (mais recentes primeiro) |
| `POST` | `/ordens` | Abre uma ordem (valida o cliente; `404` se não existir) |
| `PATCH` | `/ordens/{id}/status` | Altera apenas o status da ordem |
| `POST` | `/ordens/{id}/pecas` | Registra o uso de uma peça e baixa o estoque (`404`, `400` ou `503` em caso de falha) |

**Exemplo: abrir uma ordem**

```bash
curl -X POST https://servico-os.onrender.com/ordens \
  -H "Content-Type: application/json" \
  -d '{"cliente_id":1,"cliente_nome":"Maria Silva","placa_veiculo":"ABC1D23","descricao_servico":"Troca de óleo","valor":80}'
```

---

## 🔔 Serviço de Notificações (`notificacoes`)

Guarda as mensagens geradas pelo serviço de Ordens (ordem aberta, status alterado, peça usada).

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/notificacoes` | Recebe e grava uma notificação |
| `GET` | `/notificacoes` | Lista as notificações (mais recentes primeiro) |

---

## 🧭 Decisões de projeto (rotas que não foram criadas)

- **`DELETE /ordens/{id}`**: a ordem é um registro histórico e financeiro; o cancelamento é feito pelo status.
- **`GET /ordens/{id}` e `PUT /ordens/{id}`**: o front-end já recebe tudo na listagem e só altera o status.
- **`PUT` e `DELETE` em notificações**: são um registro do que foi enviado e não devem ser alterados.
- **Autenticação**: fora do escopo desta etapa.

---

## 🚀 Como rodar localmente

Cada serviço sobe sozinho e, sem variáveis de ambiente, usa um banco local (SQLite no Python, H2 no Java).

| Serviço | Porta |
|---|---|
| Cliente e Veículos | `8080` |
| Catálogo e Estoque | `8081` |
| Ordens de Serviço | `8001` |
| Notificações | `8002` |

**Serviços Python** (um terminal para cada, no Windows):

```bat
cd servico-os
py -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --port 8001
```

Para as notificações, repita dentro de `notificacoes` com `--port 8002`.

**Serviços Java** (um terminal para cada):

```bash
cd clientes-veiculos
./mvnw spring-boot:run
```

Repita em `catalogo-estoque`. Depois, abra o arquivo `frontend/index.html` no navegador (as URLs no início do script apontam para a nuvem; troque por `http://localhost:PORTA` para testar localmente).

---

## ⚙️ Configuração por variáveis de ambiente

| Serviço | Variáveis |
|---|---|
| `servico-os` | `DATABASE_URL`, `CLIENTES_URL`, `CATALOGO_URL`, `NOTIFICACOES_URL` |
| `notificacoes` | `DATABASE_URL` |
| `clientes-veiculos` / `catalogo-estoque` | `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD` |

> 🔒 Os valores reais (principalmente as senhas dos bancos) ficam apenas no painel da Render e **nunca** são enviados ao repositório.

---

## ☁️ Implantação

- **Serviços:** Render (Web Services com Docker), cada um a partir do seu `Dockerfile`, na branch `main`.
- **Bancos:** um projeto PostgreSQL separado na Neon para cada serviço.
- **Front-end:** Render (Static Site), publicando a pasta `frontend`.
- A cada *merge* na `main`, a Render reimplanta automaticamente.

---

## 🗂️ Trabalhos anteriores

Diagramas de classes da etapa em que o sistema ainda era um programa único:

**Diagrama v1**
![Diagrama de Classes](https://i.postimg.cc/pLBGYwqb/Diagrama-V1.jpg)

**Diagrama v2**
![Diagrama de Classes](https://i.postimg.cc/0jmZ6Fzr/Diagrama-V2.jpg)

**Diagrama v3**
![Diagrama de Classes](https://i.postimg.cc/K8mQz9x2/Diagrama-V3.png)
