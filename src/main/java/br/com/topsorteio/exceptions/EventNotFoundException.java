package br.com.topsorteio.exceptions;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(){super("Usuário não encontrado.");}

    public EventNotFoundException(String mensagem){
        super(mensagem);
    }
}
