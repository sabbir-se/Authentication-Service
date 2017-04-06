package com.dsi.authentication.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by sabbir on 6/20/16.
 */
public class CustomException extends Exception {

    private ErrorMessage errorMessage;

    public CustomException(ErrorMessage errorMessage){
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }
}
