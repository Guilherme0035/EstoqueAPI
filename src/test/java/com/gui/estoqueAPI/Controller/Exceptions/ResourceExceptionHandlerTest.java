package com.gui.estoqueAPI.Controller.Exceptions;

import com.gui.estoqueAPI.Service.Exception.BadRequestException;
import com.gui.estoqueAPI.Service.Exception.BadRequestExceptionQuantidade;
import com.gui.estoqueAPI.Service.Exception.ResourceNotFoundExceptionEstoque;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceExceptionHandlerTest {

    private static final String REQUEST_URI = "/api/estoque/sku/SKU-01";

    @InjectMocks
    private ResourceExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn(REQUEST_URI);
    }

    @Test
    void badRequest_deveRetornar400ComCorpoPadronizado() {
        var mensagem = "A quantidade de entrada para o SKU deve ser maior que zero";
        var exception = new BadRequestException(mensagem);

        var response = handler.badRequest(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals(mensagem, body.getMessage());
        assertEquals("SKU informado é inválido", body.getError());
        assertEquals(REQUEST_URI, body.getPath());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void resourceNotFoundEstoque_deveRetornar404ComCorpoPadronizado() {
        var sku = "SKU-INEXISTENTE";
        var exception = new ResourceNotFoundExceptionEstoque(sku);

        var response = handler.resourceNotFoundEstoque(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertEquals(exception.getMessage(), body.getMessage());
        assertEquals("SKU informado é inválido", body.getError());
        assertEquals(REQUEST_URI, body.getPath());
        assertNotNull(body.getTimestamp());
    }

    @Test
    void badRequestQuantidade_deveRetornar400ComCorpoPadronizado() {
        var exception = new BadRequestExceptionQuantidade("SKU-01");

        var response = handler.badRequestQuantidade(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals(exception.getMessage(), body.getMessage());
        assertEquals("Estoque insuficiente para a quantidade desejada", body.getError());
        assertEquals(REQUEST_URI, body.getPath());
        assertNotNull(body.getTimestamp());
    }

}
