package com.oficina.catalogo_estoque.servico;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@CrossOrigin(origins = "*")
public class ServicoController {

    private final ServicoRepository repository;

    public ServicoController(ServicoRepository repository) {
        this.repository = repository;
    }

    // POST /servicos -> cadastrar serviço
    @PostMapping
    public ResponseEntity<Servico> cadastrar(@Valid @RequestBody Servico servico) {
        servico.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(servico));
    }

    // GET /servicos -> listar serviços
    @GetMapping
    public List<Servico> listar() {
        return repository.findAll();
    }

    // GET /servicos/1 -> consultar um serviço
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}