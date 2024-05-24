package br.com.topsorteio.exceptions;

public class EventTimeOutException extends RuntimeException{
    public EventTimeOutException(){super("Timeout.");}

    public EventTimeOutException(String mensagem){
        super(mensagem);
    }
}
