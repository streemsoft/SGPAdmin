package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class Diaria implements Serializable {

    private String key;
    private String dataentrada;
    private String datasaida;
    private String key_quarto;
    private String key_cliente;
    private String valor;
    private String fpag;
    private String funcionario;

    public Diaria() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataloc() {
        return dataentrada;
    }

    public void setDataloc(String dataloc) {
        this.dataentrada = dataloc;
    }

    public String getKey_quarto() {
        return key_quarto;
    }

    public void setKey_quarto(String key_quarto) {
        this.key_quarto = key_quarto;
    }

    public String getKey_cliente() {
        return key_cliente;
    }

    public void setKey_cliente(String key_cliente) {
        this.key_cliente = key_cliente;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFpag() {
        return fpag;
    }

    public void setFpag(String fpag) {
        this.fpag = fpag;
    }

    public String getDatasaida() {
        return datasaida;
    }

    public void setDatasaida(String datasaida) {
        this.datasaida = datasaida;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }
}
