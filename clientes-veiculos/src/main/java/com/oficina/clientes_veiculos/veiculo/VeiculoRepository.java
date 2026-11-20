package com.oficina.clientes_veiculos.veiculo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByClienteId(Long clienteId);
}