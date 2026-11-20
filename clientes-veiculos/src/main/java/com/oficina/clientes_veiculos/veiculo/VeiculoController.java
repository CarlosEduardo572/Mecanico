package com.oficina.clientes_veiculos.veiculo;

import com.oficina.clientes_veiculos.cliente.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class VeiculoController {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    public VeiculoController(VeiculoRepository veiculoRepository,
                             ClienteRepository clienteRepository) {
        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    // POST cadastrar veículo do cliente 1
    @PostMapping("/clientes/{clienteId}/veiculos")
    public ResponseEntity<Veiculo> cadastrar(@PathVariable Long clienteId,
                                             @Valid @RequestBody Veiculo veiculo) {
        return clienteRepository.findById(clienteId)
                .map(cliente -> {
                    veiculo.setId(null);
                    veiculo.setCliente(cliente);
                    Veiculo salvo = veiculoRepository.save(veiculo);
                    return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET listar veículos do cliente 1
    @GetMapping("/clientes/{clienteId}/veiculos")
    public List<Veiculo> listarDoCliente(@PathVariable Long clienteId) {
        return veiculoRepository.findByClienteId(clienteId);
    }

    // GET consultar um veículo
    @GetMapping("/veiculos/{id}")
    public ResponseEntity<Veiculo> buscar(@PathVariable Long id) {
        return veiculoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}