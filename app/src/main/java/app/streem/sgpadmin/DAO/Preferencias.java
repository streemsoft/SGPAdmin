package app.streem.sgpadmin.DAO;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context context;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "postosp.conf";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_INDENTIFICADOR = "logadoKey";
    private final String CHAVE_NOME_USUARIO = "logadoNome";

    private final String CHAVE_KEY_POSTO_ATIVO = "postoAtivo";

    private final String CHAVE_KEY_POSTO_NOME = "postoNome";

    private final String CHAVE_KEY_FUNCIONARIO_ATIVO = "funcionarioAtivo";

    private final String CHAVE_KEY_FUNCIONARIO_NOME = "funcionarioNome";

    private final String CHAVE_KEY_TOTAL_LITROS = "totalLitros";

    private final String CHAVE_KEY_TOTAL_DINHEIRO = "totalDinheiro";



    public Preferencias(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void limpar(){
        editor.clear();
        editor.commit();
    }

    public void setUsuarioLogado(String keyUsuario, String nomeUsurio){
        editor.putString(CHAVE_INDENTIFICADOR, keyUsuario);
        editor.putString(CHAVE_NOME_USUARIO, nomeUsurio);
        editor.commit();
    }

    public String getCHAVE_INDENTIFICADOR(){
        return preferences.getString(CHAVE_INDENTIFICADOR, "1");
    }

    public String getCHAVE_NOME_USUARIO(){
        return preferences.getString(CHAVE_NOME_USUARIO, "Erro!");
    }

    public void setCHAVE_KEY_POSTO_ATIVO(String origem){
        editor.putString(CHAVE_KEY_POSTO_ATIVO, origem);
        editor.commit();
    }

    public String getCHAVE_KEY_POSTO_ATIVO(){
        return preferences.getString(CHAVE_KEY_POSTO_ATIVO,"");
    }

    public void setCHAVE_KEY_POSTO_NOME(String origem){
        editor.putString(CHAVE_KEY_POSTO_NOME, origem);
        editor.commit();
    }

    public String getCHAVE_KEY_POSTO_NOME(){
        return preferences.getString(CHAVE_KEY_POSTO_NOME,"Aguarde!");
    }



    public void setCHAVE_KEY_FUNCIONARIO_ATIVO(String origem){
        editor.putString(CHAVE_KEY_FUNCIONARIO_ATIVO, origem);
        editor.commit();
    }

    public String getCHAVE_KEY_FUNCIONARIO_ATIVO(){
        return preferences.getString(CHAVE_KEY_FUNCIONARIO_ATIVO,"");
    }

    public void setCHAVE_KEY_FUNCIONARIO_NOME(String origem){
        editor.putString(CHAVE_KEY_FUNCIONARIO_NOME, origem);
        editor.commit();
    }

    public String getCHAVE_KEY_FUNCIONARIO_NOME(){
        return preferences.getString(CHAVE_KEY_FUNCIONARIO_NOME,"Selecionar!");
    }

    public void setCHAVE_KEY_TOTAL_LITROS(String origem){
        editor.putString(CHAVE_KEY_TOTAL_LITROS, origem);
        editor.commit();
    }

    public String getCHAVE_KEY_TOTAL_LITROS(){
        return preferences.getString(CHAVE_KEY_TOTAL_LITROS,"0!");
    }

    public void setCHAVE_KEY_TOTAL_DINHEIRO(String origem){
        editor.putString(CHAVE_KEY_TOTAL_DINHEIRO, origem);
        editor.commit();
    }

    public String getCHAVE_KEY_TOTAL_DINHEIRO(){
        return preferences.getString(CHAVE_KEY_TOTAL_DINHEIRO,"0!");
    }

}
