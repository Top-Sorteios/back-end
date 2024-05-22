package br.com.topsorteio.exceptions;


public class EventBadRequesException extends RuntimeException{

    public EventBadRequesException (){super("Aconteceu algum erro.");}

    public EventBadRequesException (String mensagem){
        super(mensagem);
    }
}

