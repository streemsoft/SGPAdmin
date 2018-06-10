package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class Produto implements Serializable {

    private String key;
    private String nome;
    private String qtd;
    private String ult_compra;
    private String ult_venda;
    private String preco;

    public Produto() {
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

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }

    public String getUlt_compra() {
        return ult_compra;
    }

    public void setUlt_compra(String ult_compra) {
        this.ult_compra = ult_compra;
    }

    public String getUlt_venda() {
        return ult_venda;
    }

    public void setUlt_venda(String ult_venda) {
        this.ult_venda = ult_venda;
    }

    @Override
    public String toString(){
        return nome + " ("+qtd+")";
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
