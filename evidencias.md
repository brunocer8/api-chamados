### Teste 1 — Listar todos

**Requisição:**
```bash
curl -i http://localhost:8080/chamados
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

[{"id":1,"titulo":"Impressora não imprime","solicitante":"Ana Souza","setor":"Financeiro","prioridade":"ALTA","status":"ABERTO"},{"id":2,"titulo":"Computador não liga","solicitante":"Carlos Lima","setor":"RH","prioridade":"ALTA","status":"EM_ANDAMENTO"},{"id":3,"titulo":"Sem acesso à rede Wi-Fi","solicitante":"Marina Alves","setor":"Comercial","prioridade":"MEDIA","status":"ABERTO"},{"id":4,"titulo":"Redefinir senha do e-mail","solicitante":"João Pereira","setor":"Financeiro","prioridade":"BAIXA","status":"RESOLVIDO"},{"id":5,"titulo":"Instalar pacote Office","solicitante":"Paula Mendes","setor":"Marketing","prioridade":"BAIXA","status":"EM_ANDAMENTO"},{"id":6,"titulo":"Servidor de arquivos fora do ar","solicitante":"Roberto Dias","setor":"TI","prioridade":"ALTA","status":"ABERTO"},{"id":7,"titulo":"Impressora com papel enroscado","solicitante":"Fernanda Costa","setor":"RH","prioridade":"MEDIA","status":"RESOLVIDO"},{"id":8,"titulo":"VPN não conecta","solicitante":"Lucas Barros","setor":"Comercial","prioridade":"ALTA","status":"RESOLVIDO"},{"id":9,"titulo":"Monitor com tela piscando","solicitante":"Juliana Rocha","setor":"Marketing","prioridade":"MEDIA","status":"ABERTO"},{"id":10,"titulo":"Criar conta de acesso para estagiário","solicitante":"Pedro Nunes","setor":"TI","prioridade":"BAIXA","status":"ABERTO"},{"id":11,"titulo":"Rede lenta no setor financeiro","solicitante":"Ana Souza","setor":"Financeiro","prioridade":"MEDIA","status":"EM_ANDAMENTO"},{"id":12,"titulo":"Conta bloqueada no sistema ERP","solicitante":"Carlos Lima","setor":"RH","prioridade":"ALTA","status":"EM_ANDAMENTO"}]
```

**Explicação:** Retorna 200 com os 12 chamados iniciais em JSON.

### Teste 2 — Consultar por ID

**Requisição:**
```bash
curl -i http://localhost:8080/chamados/3
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

{"id":3,"titulo":"Sem acesso à rede Wi-Fi","solicitante":"Marina Alves","setor":"Comercial","prioridade":"MEDIA","status":"ABERTO"}
```

**Explicação:** Retorna 200 com o chamado de ID 3.

### Teste 3 — ID inexistente

**Requisição:**
```bash
curl -i http://localhost:8080/chamados/999
```

**Resposta:**
```http
HTTP/1.1 404 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

{"timestamp":"2026-11-01T01:44:31.861+00:00","status":404,"error":"Not Found","message":"Chamado não encontrado: 999","path":"/chamados/999"}
```

**Explicação:** Não há chamado 999, então a API responde 404 com mensagem de erro.

### Teste 4 — Filtrar por setor

**Requisição:**
```bash
curl -i 'http://localhost:8080/chamados/setor?nome=financeiro'
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

[{"id":1,"titulo":"Impressora não imprime","solicitante":"Ana Souza","setor":"Financeiro","prioridade":"ALTA","status":"ABERTO"},{"id":4,"titulo":"Redefinir senha do e-mail","solicitante":"João Pereira","setor":"Financeiro","prioridade":"BAIXA","status":"RESOLVIDO"},{"id":11,"titulo":"Rede lenta no setor financeiro","solicitante":"Ana Souza","setor":"Financeiro","prioridade":"MEDIA","status":"EM_ANDAMENTO"}]
```

**Explicação:** A busca ignora maiúsculas: 'financeiro' encontra o setor Financeiro.

### Teste 5 — Setor sem correspondência

**Requisição:**
```bash
curl -i 'http://localhost:8080/chamados/setor?nome=juridico'
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

[]
```

**Explicação:** Nenhum chamado do setor Juridico: retorna 200 com lista vazia [].

### Teste 6 — Filtrar por prioridade

**Requisição:**
```bash
curl -i 'http://localhost:8080/chamados/prioridade?nivel=alta'
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

[{"id":1,"titulo":"Impressora não imprime","solicitante":"Ana Souza","setor":"Financeiro","prioridade":"ALTA","status":"ABERTO"},{"id":2,"titulo":"Computador não liga","solicitante":"Carlos Lima","setor":"RH","prioridade":"ALTA","status":"EM_ANDAMENTO"},{"id":6,"titulo":"Servidor de arquivos fora do ar","solicitante":"Roberto Dias","setor":"TI","prioridade":"ALTA","status":"ABERTO"},{"id":8,"titulo":"VPN não conecta","solicitante":"Lucas Barros","setor":"Comercial","prioridade":"ALTA","status":"RESOLVIDO"},{"id":12,"titulo":"Conta bloqueada no sistema ERP","solicitante":"Carlos Lima","setor":"RH","prioridade":"ALTA","status":"EM_ANDAMENTO"}]
```

**Explicação:** Retorna apenas chamados de prioridade ALTA, mesmo com 'alta' em minúsculas.

### Teste 7 — Prioridade inválida

**Requisição:**
```bash
curl -i 'http://localhost:8080/chamados/prioridade?nivel=urgente'
```

**Resposta:**
```http
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT
Connection: close

{"timestamp":"2026-11-01T01:44:31.931+00:00","status":400,"error":"Bad Request","message":"Prioridade inválida: use BAIXA, MEDIA ou ALTA","path":"/chamados/prioridade"}
```

**Explicação:** Valor fora de BAIXA/MEDIA/ALTA gera 400 Bad Request.

### Teste 8 — Busca por título

**Requisição:**
```bash
curl -i 'http://localhost:8080/chamados/busca?texto=IMPRESSORA'
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

[{"id":1,"titulo":"Impressora não imprime","solicitante":"Ana Souza","setor":"Financeiro","prioridade":"ALTA","status":"ABERTO"},{"id":7,"titulo":"Impressora com papel enroscado","solicitante":"Fernanda Costa","setor":"RH","prioridade":"MEDIA","status":"RESOLVIDO"}]
```

**Explicação:** 'IMPRESSORA' em maiúsculas encontra títulos com 'Impressora', pois a busca ignora caixa.

### Teste 9 — Chamados críticos

**Requisição:**
```bash
curl -i http://localhost:8080/chamados/criticos
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

[{"id":1,"titulo":"Impressora não imprime","solicitante":"Ana Souza","setor":"Financeiro","prioridade":"ALTA","status":"ABERTO"},{"id":2,"titulo":"Computador não liga","solicitante":"Carlos Lima","setor":"RH","prioridade":"ALTA","status":"EM_ANDAMENTO"},{"id":6,"titulo":"Servidor de arquivos fora do ar","solicitante":"Roberto Dias","setor":"TI","prioridade":"ALTA","status":"ABERTO"},{"id":12,"titulo":"Conta bloqueada no sistema ERP","solicitante":"Carlos Lima","setor":"RH","prioridade":"ALTA","status":"EM_ANDAMENTO"}]
```

**Explicação:** Combina prioridade ALTA e status diferente de RESOLVIDO.

### Teste 10 — Resumo por status

**Requisição:**
```bash
curl -i http://localhost:8080/chamados/resumo
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:31 GMT

{"abertos":5,"emAndamento":4,"resolvidos":3,"total":12}
```

**Explicação:** Retorna as quantidades de abertos, em andamento, resolvidos e o total.

### Teste 11 — Cadastrar chamado (POST)

**Requisição:**
```bash
curl -i -X POST http://localhost:8080/chamados -H 'Content-Type: application/json' -d '{"titulo":"Teclado sem funcionar","solicitante":"Maria Silva","setor":"Vendas","prioridade":"MEDIA"}'
```

**Resposta:**
```http
HTTP/1.1 201 
Location: /chamados/13
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:32 GMT

{"id":13,"titulo":"Teclado sem funcionar","solicitante":"Maria Silva","setor":"Vendas","prioridade":"MEDIA","status":"ABERTO"}
```

**Explicação:** Recebe o JSON, gera o ID 13 e responde 201 Created com o header Location.

### Teste 12 — Verificar cadastro

**Requisição:**
```bash
curl -i http://localhost:8080/chamados/13
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:32 GMT

{"id":13,"titulo":"Teclado sem funcionar","solicitante":"Maria Silva","setor":"Vendas","prioridade":"MEDIA","status":"ABERTO"}
```

**Explicação:** O GET do ID 13 devolve o chamado recém-criado, com status ABERTO por padrão.

### Teste 13 — Resumo após cadastro

**Requisição:**
```bash
curl -i http://localhost:8080/chamados/resumo
```

**Resposta:**
```http
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 01 Nov 2026 01:44:32 GMT

{"abertos":6,"emAndamento":4,"resolvidos":3,"total":13}
```

**Explicação:** O total passou de 12 para 13 e os abertos de 5 para 6.

