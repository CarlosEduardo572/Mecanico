package com.oficina.catalogo_estoque.peca;

import jakarta.validation.constraints.NotNull;

public record AjusteEstoqueRequest(@NotNull Integer quantidade) {
}