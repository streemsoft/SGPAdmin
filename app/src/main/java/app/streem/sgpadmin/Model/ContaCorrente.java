package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class ContaCorrente implements Serializable {

    private String key;
    private String valor;
    private String datacad;
    private String origem;

    public ContaCorrente() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDatacad() {
        return datacad;
    }

    public void setDatacad(String datacad) {
        this.datacad = datacad;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
