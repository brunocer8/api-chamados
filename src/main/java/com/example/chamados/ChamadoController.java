package com.example.chamados;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    // "Banco de dados" em memória: some quando a aplicação é encerrada
    private final List<Chamado> chamados = new CopyOnWriteArrayList<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    @PostConstruct
    void carregarDadosIniciais() {
        adicionar("Impressora não imprime", "Ana Souza", "Financeiro", Prioridade.ALTA, Status.ABERTO);
        adicionar("Computador não liga", "Carlos Lima", "RH", Prioridade.ALTA, Status.EM_ANDAMENTO);
        adicionar("Sem acesso à rede Wi-Fi", "Marina Alves", "Comercial", Prioridade.MEDIA, Status.ABERTO);
        adicionar("Redefinir senha do e-mail", "João Pereira", "Financeiro", Prioridade.BAIXA, Status.RESOLVIDO);
        adicionar("Instalar pacote Office", "Paula Mendes", "Marketing", Prioridade.BAIXA, Status.EM_ANDAMENTO);
        adicionar("Servidor de arquivos fora do ar", "Roberto Dias", "TI", Prioridade.ALTA, Status.ABERTO);
        adicionar("Impressora com papel enroscado", "Fernanda Costa", "RH", Prioridade.MEDIA, Status.RESOLVIDO);
        adicionar("VPN não conecta", "Lucas Barros", "Comercial", Prioridade.ALTA, Status.RESOLVIDO);
        adicionar("Monitor com tela piscando", "Juliana Rocha", "Marketing", Prioridade.MEDIA, Status.ABERTO);
        adicionar("Criar conta de acesso para estagiário", "Pedro Nunes", "TI", Prioridade.BAIXA, Status.ABERTO);
        adicionar("Rede lenta no setor financeiro", "Ana Souza", "Financeiro", Prioridade.MEDIA, Status.EM_ANDAMENTO);
        adicionar("Conta bloqueada no sistema ERP", "Carlos Lima", "RH", Prioridade.ALTA, Status.EM_ANDAMENTO);
    }

    private void adicionar(String titulo, String solicitante, String setor,
                           Prioridade prioridade, Status status) {
        chamados.add(new Chamado(proximoId.getAndIncrement(), titulo, solicitante, setor, prioridade, status));
    }

    // GET /chamados
    @GetMapping
    public List<Chamado> listar() {
        return chamados;
    }

    // GET /chamados/{id}
    @GetMapping("/{id}")
    public Chamado buscarPorId(@PathVariable Long id) {
        return chamados.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Chamado não encontrado: " + id));
    }

    // GET /chamados/setor?nome=financeiro
    @GetMapping("/setor")
    public List<Chamado> porSetor(@RequestParam String nome) {
        return chamados.stream()
                .filter(c -> c.getSetor().equalsIgnoreCase(nome.trim()))
                .toList();
    }

    // GET /chamados/prioridade?nivel=alta
    @GetMapping("/prioridade")
    public List<Chamado> porPrioridade(@RequestParam String nivel) {
        Prioridade prioridade;
        try {
            prioridade = Prioridade.from(nivel);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Prioridade inválida: use BAIXA, MEDIA ou ALTA");
        }
        return chamados.stream()
                .filter(c -> c.getPrioridade() == prioridade)
                .toList();
    }

    // GET /chamados/busca?texto=impressora
    @GetMapping("/busca")
    public List<Chamado> buscarPorTitulo(@RequestParam String texto) {
        String termo = texto.trim().toLowerCase(Locale.ROOT);
        return chamados.stream()
                .filter(c -> c.getTitulo().toLowerCase(Locale.ROOT).contains(termo))
                .toList();
    }

    // GET /chamados/criticos  -> prioridade ALTA e status diferente de RESOLVIDO
    @GetMapping("/criticos")
    public List<Chamado> criticos() {
        return chamados.stream()
                .filter(c -> c.getPrioridade() == Prioridade.ALTA && c.getStatus() != Status.RESOLVIDO)
                .toList();
    }

    // GET /chamados/resumo
    @GetMapping("/resumo")
    public Map<String, Long> resumo() {
        long abertos = contar(Status.ABERTO);
        long emAndamento = contar(Status.EM_ANDAMENTO);
        long resolvidos = contar(Status.RESOLVIDO);

        Map<String, Long> resumo = new LinkedHashMap<>();
        resumo.put("abertos", abertos);
        resumo.put("emAndamento", emAndamento);
        resumo.put("resolvidos", resolvidos);
        resumo.put("total", (long) chamados.size());
        return resumo;
    }

    private long contar(Status status) {
        return chamados.stream().filter(c -> c.getStatus() == status).count();
    }

    // POST /chamados
    @PostMapping
    public ResponseEntity<Chamado> cadastrar(@RequestBody Chamado novo) {
        if (vazio(novo.getTitulo()) || vazio(novo.getSolicitante())
                || vazio(novo.getSetor()) || novo.getPrioridade() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Campos obrigatórios: titulo, solicitante, setor e prioridade");
        }
        novo.setId(proximoId.getAndIncrement());          // o cliente não escolhe o ID
        if (novo.getStatus() == null) {
            novo.setStatus(Status.ABERTO);                // padrão para chamado novo
        }
        chamados.add(novo);
        return ResponseEntity.created(URI.create("/chamados/" + novo.getId())).body(novo);
    }

    private boolean vazio(String s) {
        return s == null || s.isBlank();
    }
}
