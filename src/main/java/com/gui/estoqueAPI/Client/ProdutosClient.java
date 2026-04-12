package com.gui.estoqueAPI.Client;

import com.gui.estoqueAPI.Config.FeignConfig;
import com.gui.estoqueAPI.Entity.DTO.ProdutoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "produtosClient",
        url = "${produtos.api.url}",
        configuration = FeignConfig.class
)
public interface ProdutosClient {

    @GetMapping("/api/products/{sku}")
    ProdutoDTO verifyProduct(@PathVariable String sku);

}
