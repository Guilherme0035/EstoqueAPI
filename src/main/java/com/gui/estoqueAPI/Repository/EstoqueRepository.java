package com.gui.estoqueAPI.Repository;

import com.gui.estoqueAPI.Entity.DTO.EstoqueResponse;
import com.gui.estoqueAPI.Entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository <Estoque, String> {

    Boolean existsBySku(String sku);

    Optional<Estoque> findBySku(String sku);
}
