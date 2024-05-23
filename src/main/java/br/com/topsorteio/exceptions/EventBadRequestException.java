package br.com.topsorteio.exceptions;


public class EventBadRequestException extends RuntimeException{

    public EventBadRequestException(){super("Aconteceu algum erro.");}

    public EventBadRequestException(String mensagem){
        super(mensagem);
    }
}

