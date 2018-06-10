package app.streem.sgpadmin.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Recibo implements Serializable {

    private String key;
    private String valor;
    private String datapag;
    private String key_cliente;

    public Recibo() {
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

    public String getDatapag() {
        return datapag;
    }

    public void setDatapag(String datapag) {
        this.datapag = datapag;
    }

    public String getKey_cliente() {
        return key_cliente;
    }

    public void setKey_cliente(String key_cliente) {
        this.key_cliente = key_cliente;
    }

    @Override
    public String toString(){
        return converterData(datapag) + " - Valor: "+ valor;
    }

    public String converterData(String da){
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        return sdf.format(new Date(Long.parseLong(da)));
    }
}
