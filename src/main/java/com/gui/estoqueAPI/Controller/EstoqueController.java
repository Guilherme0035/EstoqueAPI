package com.gui.estoqueAPI.Controller;

import com.gui.estoqueAPI.Entity.DTO.EstoqueRequest;
import com.gui.estoqueAPI.Entity.DTO.EstoqueResponse;
import com.gui.estoqueAPI.Service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;


    @PostMapping
    @PreAuthorize("hasAuthority('PRODUTOS_WRITE')")
    public ResponseEntity<EstoqueResponse> createEstoque(@RequestBody EstoqueRequest body){
        Date data = Date.from(Instant.now());
        var estoque = estoqueService.criarEstoque(body.sku(),body.quantidade());
        return ResponseEntity.ok().body(estoque);
    }

    @GetMapping("/sku/{sku}")
    @PreAuthorize("hasAuthority('PRODUTOS_READ')")
    public EstoqueResponse consultarEstoqueSku (@PathVariable String sku){
        return estoqueService.consultarEstoqueSku(sku);
    }
}
