package com.gui.estoqueAPI.Service.Exception;

import java.io.Serial;

public class BadRequestExceptionQuantidade extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestExceptionQuantidade(Object id){
        super("O produto não possui estoque suficiente");
    }

}
