package br.com.topsorteio.exceptions.exceptionhandler;

import br.com.topsorteio.exceptions.EventServiceUnavailableException;
import br.com.topsorteio.exceptions.EventTimeOutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EventTimeOutException.class)
    private ResponseEntity<RestErrorMensagem> eventTimeOutHandler(EventTimeOutException exception){
        RestErrorMensagem threatResponse = new RestErrorMensagem(HttpStatus.REQUEST_TIMEOUT, 408, exception.getMessage(), false );
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(threatResponse);
    }

    @ExceptionHandler(EventServiceUnavailableException.class)
    private ResponseEntity<RestErrorMensagem> eventServiceUnavailableHandler(EventServiceUnavailableException exception){
        RestErrorMensagem threatResponse = new RestErrorMensagem(HttpStatus.SERVICE_UNAVAILABLE, 503, exception.getMessage(), false );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(threatResponse);
    }

}
