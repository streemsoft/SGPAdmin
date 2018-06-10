package app.streem.sgpadmin;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Preco;

public class CadPrecosActivity extends AppCompatActivity {

    private Preco preco;
    private String key_controle;
    private TextInputEditText combustivel;
    private TextInputEditText valor;
    private Button botao_salvar;
    private Preferencias preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_precos);
        getSupportActionBar().setTitle("Manutenção de Preços");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        //=====================  INST. OBJ. =========================

        combustivel = findViewById(R.id.cadprecocombustivel);
        valor = findViewById(R.id.cadprecovalor);
        botao_salvar = findViewById(R.id.cadprecosalvar);
        preferencias = new Preferencias(this);


        //==========================================================

        Intent dados = getIntent();

        String acao = dados.getStringExtra("acao");

        if(acao.equals("1")){
            key_controle = "1";
            preco = new Preco();
        }else{
            key_controle = "2";
            preco = (Preco) dados.getSerializableExtra("obj");
            combustivel.setText(preco.getCombustivel());
            valor.setText(preco.getValor());
        }

        botao_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (combustivel.getText().toString().equals("") | valor.getText().toString().equals("")) {
                    Toast.makeText(CadPrecosActivity.this, "Dados incompletos!", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    if (key_controle.equals("1")) {
                        preco.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRECOS").push().getKey());
                        preco.setValor(valor.getText().toString());
                        preco.setCombustivel(combustivel.getText().toString());
                        preco.setData(formato.format(new Date()));
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRECOS").child(preco.getKey()).setValue(preco);
                        Toast.makeText(CadPrecosActivity.this, "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                        key_controle = "2";
                    } else {
                        preco.setValor(valor.getText().toString());
                        preco.setCombustivel(combustivel.getText().toString());
                        preco.setData(formato.format(new Date()));
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRECOS").child(preco.getKey()).setValue(preco);
                        Toast.makeText(CadPrecosActivity.this, "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                        key_controle = "2";
                    }
                }
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();
                break;
            default:break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        finish();
        return;
    }
}
