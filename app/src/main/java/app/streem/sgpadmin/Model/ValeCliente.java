package app.streem.sgpadmin.Model;

import java.io.Serializable;

public class ValeCliente implements Serializable {

    private String key;
    private String situacao;

    public ValeCliente() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
