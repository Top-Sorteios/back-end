package br.com.topsorteio.exceptions;

public class EventBadRequestException extends RuntimeException{
    public EventBadRequestException(){super("Erro na requisição");}

    public EventBadRequestException(String mensagem){
        super(mensagem);
    }
}
