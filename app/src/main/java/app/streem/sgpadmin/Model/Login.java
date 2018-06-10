package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class Login implements Serializable {

    private String key;
    private String key_posto;
    private String nome;

    public Login() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey_posto() {
        return key_posto;
    }

    public void setKey_posto(String key_posto) {
        this.key_posto = key_posto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
