package com.gui.estoqueAPI.Service;

import com.gui.estoqueAPI.Client.ProdutosClient;
import com.gui.estoqueAPI.Entity.DTO.EstoqueResponse;
import com.gui.estoqueAPI.Entity.DTO.ProdutoDTO;
import com.gui.estoqueAPI.Entity.Estoque;
import com.gui.estoqueAPI.Repository.EstoqueRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutosClient produtosClient;


    private ProdutoDTO buscarProdutoPorSku(String sku){
        try {
             return produtosClient.verifyProduct(sku);
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("Sku não localizado");
        }
    }


    public EstoqueResponse criarEstoque(String sku, Integer quantidade) {
        ProdutoDTO skuFind;

        try {
            skuFind = buscarProdutoPorSku(sku);
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("Produto não encontrado para esse SKU");
        }


        if (estoqueRepository.existsBySku(sku)) {
            throw new IllegalStateException("Estoque já criado para esse SKU");
        }

        Estoque newEstoque = new Estoque(sku,quantidade);
        estoqueRepository.save(newEstoque);

        return new EstoqueResponse(skuFind.name(),sku,quantidade);
    }


    public EstoqueResponse consultarEstoqueSku (String sku){
        Estoque estoque = estoqueRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Sku não possue estoque"));


        ProdutoDTO produto = buscarProdutoPorSku(sku);

        return new EstoqueResponse(produto.name(),sku, estoque.getQuantidade());
    }


}
