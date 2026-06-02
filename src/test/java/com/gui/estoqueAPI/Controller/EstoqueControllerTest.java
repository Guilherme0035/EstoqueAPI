package com.gui.estoqueAPI.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gui.estoqueAPI.Entity.DTO.EntradaEstoqueRequest;
import com.gui.estoqueAPI.Entity.DTO.EstoqueRequest;
import com.gui.estoqueAPI.Entity.DTO.EstoqueResponse;
import com.gui.estoqueAPI.Entity.DTO.ProdutoDTO;
import com.gui.estoqueAPI.Entity.Estoque;
import com.gui.estoqueAPI.Service.EstoqueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstoqueController.class)
@AutoConfigureMockMvc(addFilters = false)
class EstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EstoqueService estoqueService;

    @Test
    void createEstoque_deveRetornar200ComDadosDoEstoque() throws Exception {
        var sku = "SKU-01";
        var request = new EstoqueRequest(sku, 10);
        var response = new EstoqueResponse("Produto Teste", sku, 10);

        when(estoqueService.criarEstoque(sku, 10)).thenReturn(response);

        mockMvc.perform(post("/api/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Teste"))
                .andExpect(jsonPath("$.sku").value(sku))
                .andExpect(jsonPath("$.quantidade").value(10));

        verify(estoqueService).criarEstoque(sku, 10);
    }

    @Test
    void consultarEstoqueSku_deveRetornarDadosDoEstoque() throws Exception {
        var sku = "SKU-02";
        var response = new EstoqueResponse("Produto Consulta", sku, 25);

        when(estoqueService.consultarEstoqueSku(sku)).thenReturn(response);

        mockMvc.perform(get("/api/estoque/sku/{sku}", sku))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Consulta"))
                .andExpect(jsonPath("$.sku").value(sku))
                .andExpect(jsonPath("$.quantidade").value(25));

        verify(estoqueService).consultarEstoqueSku(sku);
    }

    @Test
    void entradaEstoque_deveRetornarEstoqueAtualizadoComNomeDoProduto() throws Exception {
        var sku = "SKU-03";
        var request = new EntradaEstoqueRequest(5);
        var estoqueAtualizado = new Estoque(sku, 15);
        var produto = new ProdutoDTO(sku, "Produto Entrada", BigDecimal.TEN);

        when(estoqueService.entradaEstoque(eq(sku), eq(5))).thenReturn(estoqueAtualizado);
        when(estoqueService.buscarProdutoPorSku(sku)).thenReturn(produto);

        mockMvc.perform(put("/api/estoque/sku/{sku}/entrada", sku)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Entrada"))
                .andExpect(jsonPath("$.sku").value(sku))
                .andExpect(jsonPath("$.quantidade").value(15));

        verify(estoqueService).entradaEstoque(sku, 5);
        verify(estoqueService).buscarProdutoPorSku(sku);
    }

    @Test
    void saidaEstoque_deveRetornarEstoqueAtualizadoComNomeDoProduto() throws Exception {
        var sku = "SKU-04";
        var request = new EntradaEstoqueRequest(3);
        var estoqueAtualizado = new Estoque(sku, 7);
        var produto = new ProdutoDTO(sku, "Produto Saida", BigDecimal.ONE);

        when(estoqueService.saidaEstoque(eq(sku), eq(3))).thenReturn(estoqueAtualizado);
        when(estoqueService.buscarProdutoPorSku(sku)).thenReturn(produto);

        mockMvc.perform(put("/api/estoque/sku/{sku}/saida", sku)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Saida"))
                .andExpect(jsonPath("$.sku").value(sku))
                .andExpect(jsonPath("$.quantidade").value(7));

        verify(estoqueService).saidaEstoque(sku, 3);
        verify(estoqueService).buscarProdutoPorSku(sku);
    }

}
