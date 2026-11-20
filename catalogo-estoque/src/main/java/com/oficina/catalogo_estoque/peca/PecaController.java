package com.oficina.catalogo_estoque.peca;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pecas")
@CrossOrigin(origins = "*")
public class PecaController {

    private final PecaRepository repository;

    public PecaController(PecaRepository repository) {
        this.repository = repository;
    }

    // POST /pecas -> cadastrar peça
    @PostMapping
    public ResponseEntity<Peca> cadastrar(@Valid @RequestBody Peca peca) {
        peca.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(peca));
    }

    // GET /pecas -> listar peças
    @GetMapping
    public List<Peca> listar() {
        return repository.findAll();
    }

    // GET /pecas/1 -> consultar uma peça
    @GetMapping("/{id}")
    public ResponseEntity<Peca> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /pecas/1 -> atualizar dados,o estoque não muda por aqui
    @PutMapping("/{id}")
    public ResponseEntity<Peca> atualizar(@PathVariable Long id,
                                          @Valid @RequestBody Peca dados) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setNome(dados.getNome());
                    existente.setCodigo(dados.getCodigo());
                    existente.setPreco(dados.getPreco());
                    existente.setEstoqueMinimo(dados.getEstoqueMinimo());
                    existente.setAtivo(dados.isAtivo());
                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /pecas/1/estoque -> consultar quantidade
    @GetMapping("/{id}/estoque")
    public ResponseEntity<?> consultarEstoque(@PathVariable Long id) {
        Optional<Peca> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Peca p = opt.get();
        return ResponseEntity.ok(Map.of(
                "id", p.getId(),
                "nome", p.getNome(),
                "quantidadeEstoque", p.getQuantidadeEstoque(),
                "estoqueMinimo", p.getEstoqueMinimo(),
                "abaixoDoMinimo", p.getQuantidadeEstoque() < p.getEstoqueMinimo()
        ));
    }

    // PATCH /pecas/1/estoque -> {"quantidade": -2} dá baixa, {"quantidade": 10} repõe
    @PatchMapping("/{id}/estoque")
    public ResponseEntity<?> ajustarEstoque(@PathVariable Long id,
                                            @Valid @RequestBody AjusteEstoqueRequest req) {
        Optional<Peca> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Peca peca = opt.get();
        int novo = peca.getQuantidadeEstoque() + req.quantidade();
        if (novo < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Estoque insuficiente"));
        }
        peca.setQuantidadeEstoque(novo);
        return ResponseEntity.ok(repository.save(peca));
    }
}