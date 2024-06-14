package br.com.topsorteio.entities;

public enum SorteioSurpresa {
    DEFAULT(0),
    SURPRESA(1);
    private int sorteio;

    SorteioSurpresa(int sorteio){
        this.sorteio = sorteio;
    }

    public int getRole(){
        return sorteio;
    }
}
