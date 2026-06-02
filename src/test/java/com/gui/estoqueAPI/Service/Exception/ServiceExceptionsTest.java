package com.gui.estoqueAPI.Service.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceExceptionsTest {

    @Test
    void badRequestException_deveUsarMensagemDoIdInformado() {
        var mensagem = "Estoque já criado para esse produto ";

        var exception = new BadRequestException(mensagem);

        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    void badRequestExceptionQuantidade_deveRetornarMensagemFixa() {
        var exception = new BadRequestExceptionQuantidade("SKU-01");

        assertEquals("O produto não possui estoque suficiente", exception.getMessage());
    }

    @Test
    void resourceNotFoundExceptionEstoque_deveIncluirSkuNaMensagem() {
        var sku = "SKU-99";

        var exception = new ResourceNotFoundExceptionEstoque(sku);

        assertEquals("Não existe estoque para o seguinte SKU: " + sku, exception.getMessage());
    }

}
