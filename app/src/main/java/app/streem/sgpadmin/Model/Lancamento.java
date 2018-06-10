package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class Lancamento implements Serializable {

    private String key;
    private String valor;
    private String litro_qtd;
    private int tipo; //abastecimento - produto - despesa
    private int fpag; //dinheiro - cartao - vale
    private String funcionario; //funcionario ou cliente
    private String datacad;

    public Lancamento() {
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

    public String getLitro_qtd() {
        return litro_qtd;
    }

    public void setLitro_qtd(String litro_qtd) {
        this.litro_qtd = litro_qtd;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getFpag() {
        return fpag;
    }

    public void setFpag(int fpag) {
        this.fpag = fpag;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getDatacad() {
        return datacad;
    }

    public void setDatacad(String datacad) {
        this.datacad = datacad;
    }
}
