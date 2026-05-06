package com.gui.estoqueAPI.Service;

import com.gui.estoqueAPI.Client.ProdutosClient;
import com.gui.estoqueAPI.Entity.DTO.EstoqueResponse;
import com.gui.estoqueAPI.Entity.DTO.ProdutoDTO;
import com.gui.estoqueAPI.Entity.Estoque;
import com.gui.estoqueAPI.Repository.EstoqueRepository;
import com.gui.estoqueAPI.Service.Exception.BadRequestException;
import com.gui.estoqueAPI.Service.Exception.BadRequestExceptionQuantidade;
import com.gui.estoqueAPI.Service.Exception.ResourceNotFoundExceptionEstoque;
import feign.FeignException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutosClient produtosClient;


    public ProdutoDTO buscarProdutoPorSku(String sku){
        try {
             return produtosClient.verifyProduct(sku);
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("Sku não localizado");
        }
    }

    Estoque buscarEstoquePorSku(String sku) {
        return estoqueRepository.findById(sku)
                .orElseThrow(() -> new ResourceNotFoundExceptionEstoque(sku));
    }


    public EstoqueResponse criarEstoque(String sku, Integer quantidade) {
        ProdutoDTO skuFind;

        try {
            skuFind = buscarProdutoPorSku(sku);
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("Produto não encontrado para esse SKU");
        }


        if (estoqueRepository.existsBySku(sku)) {
            throw new BadRequestException("Estoque já criado para esse produto ");
        }

        Estoque newEstoque = new Estoque(sku,quantidade);
        estoqueRepository.save(newEstoque);

        return new EstoqueResponse(skuFind.name(),sku,quantidade);
    }


    public EstoqueResponse consultarEstoqueSku (String sku){
        Estoque estoque = estoqueRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundExceptionEstoque(sku));

        ProdutoDTO produto = buscarProdutoPorSku(sku);

        return new EstoqueResponse(produto.name(),sku, estoque.getQuantidade());
    }

    public Estoque entradaEstoque(@NotNull String sku, Integer quantidade) {

        if (quantidade == null || quantidade <= 0) {
            throw new BadRequestException("A quantidade de entrada para o SKU deve ser maior que zero");
        }

        Estoque estoque = buscarEstoquePorSku(sku);

        estoque.setQuantidade(estoque.getQuantidade() + quantidade);

        return estoqueRepository.save(estoque);
    }

    public Estoque saidaEstoque(@NotNull String sku, Integer quantidade){

        Estoque estoque = buscarEstoquePorSku(sku);
        var quantEstoque = estoque.quantidade;

        if (quantidade == null || quantEstoque < quantidade){
            throw new BadRequestExceptionQuantidade(sku);
        }

        var newQuant = quantEstoque - quantidade;
        estoque.setQuantidade(newQuant);

        return estoqueRepository.save(estoque);

    }

}
