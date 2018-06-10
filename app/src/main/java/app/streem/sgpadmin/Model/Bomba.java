package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class Bomba implements Serializable {

    private String key;
    private String nome;
    private String contagem;
    private String cont_final;
    private String status;
    private String preco;

    public Bomba() {
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

    public String getContagem() {
        return contagem;
    }

    public void setContagem(String contagem) {
        this.contagem = contagem;
    }

    public String getCont_final() {
        return cont_final;
    }

    public void setCont_final(String cont_final) {
        this.cont_final = cont_final;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString(){
        return nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
