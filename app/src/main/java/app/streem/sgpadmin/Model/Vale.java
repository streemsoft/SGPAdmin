package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class Vale implements Serializable{

    private String key;
    private String data;
    private String valor_total;
    private String valor_pago;
    private String valor_acrecimo;
    private String valor_desconto;
    private String status;
    private String origem;
    private String key_cliente;
    private String atual;

    public Vale() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValor_total() {
        return valor_total;
    }

    public void setValor_total(String valor_total) {
        this.valor_total = valor_total;
    }

    public String getValor_pago() {
        return valor_pago;
    }

    public void setValor_pago(String valor_pago) {
        this.valor_pago = valor_pago;
    }

    public String getValor_acrecimo() {
        return valor_acrecimo;
    }

    public void setValor_acrecimo(String valor_acrecimo) {
        this.valor_acrecimo = valor_acrecimo;
    }

    public String getValor_desconto() {
        return valor_desconto;
    }

    public void setValor_desconto(String valor_desconto) {
        this.valor_desconto = valor_desconto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getKey_cliente() {
        return key_cliente;
    }

    public void setKey_cliente(String key_cliente) {
        this.key_cliente = key_cliente;
    }

    public String getAtual() {
        return atual;
    }

    public void setAtual(String atual) {
        this.atual = atual;
    }
}
