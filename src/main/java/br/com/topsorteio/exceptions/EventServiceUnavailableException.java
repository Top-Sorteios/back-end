package br.com.topsorteio.exceptions;

public class EventServiceUnavailableException extends RuntimeException{
    public EventServiceUnavailableException(){super("Serivce Unavailable.");}

    public EventServiceUnavailableException(String mensagem){
        super(mensagem);
    }
}
