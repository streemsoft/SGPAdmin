package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class Frentista implements Serializable {

    private String key;
    private String nome;

    public Frentista() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString(){
        return nome;
    }
}
