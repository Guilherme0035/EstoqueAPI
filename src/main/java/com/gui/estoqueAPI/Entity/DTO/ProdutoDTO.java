package com.gui.estoqueAPI.Entity.DTO;

import java.math.BigDecimal;

public record ProdutoDTO(
        String sku,
        String name,
        BigDecimal preco
) {}

