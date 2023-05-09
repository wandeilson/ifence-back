package br.edu.ifpb.dac.groupd.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class FenceNameAlreadyInUseException extends AbstractException{

    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public FenceNameAlreadyInUseException(){
        super(String.format("Nome da Fence já em uso."));
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
