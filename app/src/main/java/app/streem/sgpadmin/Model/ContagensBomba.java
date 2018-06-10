package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class ContagensBomba implements Serializable {

    private String key;
    private String datacad;
    private String litros;
    private String key_bomba;
    private String valor;

    public ContagensBomba() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDatacad() {
        return datacad;
    }

    public void setDatacad(String datacad) {
        this.datacad = datacad;
    }

    public String getLitros() {
        return litros;
    }

    public void setLitros(String litros) {
        this.litros = litros;
    }

    public String getKey_bomba() {
        return key_bomba;
    }

    public void setKey_bomba(String key_bomba) {
        this.key_bomba = key_bomba;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
