package br.com.topsorteio.exceptions;

public class EventNotFoundException extends RuntimeException{


    public EventNotFoundException(){super("Não encontrado.");}

    public EventNotFoundException(String mensagem){
        super(mensagem);
    }
}
