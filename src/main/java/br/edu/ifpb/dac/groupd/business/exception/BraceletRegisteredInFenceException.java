package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class BraceletRegisteredInFenceException extends AbstractException{

    private static final HttpStatus status = HttpStatus.UNAUTHORIZED;

    public BraceletRegisteredInFenceException(String msg){
        super(String.format("Bracelet registrado em alguma fence" + msg));
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
