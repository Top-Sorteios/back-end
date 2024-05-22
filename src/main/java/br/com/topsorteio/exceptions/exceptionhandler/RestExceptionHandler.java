package br.com.topsorteio.exceptions.exceptionhandler;

import br.com.topsorteio.exceptions.EventBadRequesException;
import br.com.topsorteio.exceptions.EventNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EventNotFoundException.class)
    private ResponseEntity<RestErrorMensagem> eventNotFoundHandler(EventNotFoundException exception){
        RestErrorMensagem threatResponse = new RestErrorMensagem(HttpStatus.NOT_FOUND, 404, exception.getMessage(), false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
    }

    @ExceptionHandler(EventBadRequesException.class)
    private ResponseEntity<RestErrorMensagem> eventBadRequestHandler(EventBadRequesException exception){
        RestErrorMensagem threatResponse = new RestErrorMensagem(HttpStatus.BAD_REQUEST, 400, exception.getMessage(), false );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatResponse);
    }

}
