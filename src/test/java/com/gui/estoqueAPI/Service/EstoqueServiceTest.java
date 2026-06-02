package com.gui.estoqueAPI.Service;

import com.gui.estoqueAPI.Client.ProdutosClient;
import com.gui.estoqueAPI.Entity.Estoque;
import com.gui.estoqueAPI.Repository.EstoqueRepository;
import com.gui.estoqueAPI.Service.Exception.BadRequestException;
import com.gui.estoqueAPI.Service.Exception.BadRequestExceptionQuantidade;
import com.gui.estoqueAPI.Service.Exception.ResourceNotFoundExceptionEstoque;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private ProdutosClient produtosClient;

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    void entradaEstoque_deveLancarExcecaoQuandoQuantidadeForNulaZeroOuNegativa() {
        assertThrows(BadRequestException.class,
                () -> estoqueService.entradaEstoque("SKU-01", null));

        assertThrows(BadRequestException.class,
                () -> estoqueService.entradaEstoque("SKU-01", 0));

        assertThrows(BadRequestException.class,
                () -> estoqueService.entradaEstoque("SKU-01", - 1));
    }

    @Test
    void entradaEstoque_deveSomarQuantidadeAoEstoque() {
        var sku = "SKU-02";
        var estoque = new Estoque(sku, 10);

        when(estoqueRepository.findById(sku)).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(any(Estoque.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = estoqueService.entradaEstoque(sku, 5);
        assertEquals(15, resultado.getQuantidade());
        verify(estoqueRepository).save(estoque);
    }


    @Test
    void saidaEstoque_deveLancarExcecaoQuandoQuantidadeForNula() {
        var sku = "SKU-01";
        var estoque = new Estoque(sku, 10);
        when(estoqueRepository.findById(sku)).thenReturn(Optional.of(estoque));

        assertThrows(BadRequestExceptionQuantidade.class,
                () -> estoqueService.saidaEstoque(sku, null));
    }

    @Test
    void saidaEstoque_deveLancarExcecaoQuandoQuantidadeMaiorQueEstoque() {
        var sku = "SKU-01";
        var estoque = new Estoque(sku, 10);
        when(estoqueRepository.findById(sku)).thenReturn(Optional.of(estoque));

        assertThrows(BadRequestExceptionQuantidade.class,
                () -> estoqueService.saidaEstoque(sku, 15));
    }

    @Test
    void saidaEstoque_deveLancarExcecaoQuandoSkuNaoExistir() {
        when(estoqueRepository.findById("SKU-01")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundExceptionEstoque.class,
                () -> estoqueService.saidaEstoque("SKU-01", 5));
    }

    @Test
    void saidaEstoque_deveSubtrairQuantidadeAoEstoque() {
        var sku = "SKU-01";
        var estoque = new Estoque(sku, 10);
        when(estoqueRepository.findById(sku)).thenReturn(Optional.of(estoque));
        when(estoqueRepository.save(any(Estoque.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = estoqueService.saidaEstoque(sku, 5);
        assertEquals(5, resultado.getQuantidade());
        verify(estoqueRepository).save(estoque);
    }

}
