package com.gui.estoqueAPI.Service.Exception;

import java.io.Serial;

public class BadRequestException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException(Object id){
        super(""+ id);
    }
}
