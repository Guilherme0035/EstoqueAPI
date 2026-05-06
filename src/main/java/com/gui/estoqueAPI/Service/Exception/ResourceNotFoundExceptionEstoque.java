package com.gui.estoqueAPI.Service.Exception;


import java.io.Serial;

public class ResourceNotFoundExceptionEstoque extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundExceptionEstoque(String sku){
        super("Não existe estoque para o seguinte SKU: " + sku);
    }
}
