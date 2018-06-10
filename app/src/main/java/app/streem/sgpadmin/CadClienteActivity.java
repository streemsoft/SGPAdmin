package app.streem.sgpadmin;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.ClienteDados;
import app.streem.sgpadmin.Model.ClienteLista;
import app.streem.sgpadmin.Model.Preco;

public class CadClienteActivity extends AppCompatActivity {

    private TextInputEditText nome;
    private TextInputEditText telefone;
    private TextInputEditText obs;
    private Button botao_salvar;
    private String key_controle;
    private ClienteLista clienteLista;
    private ClienteDados clienteDados;
    private Preferencias preferencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_cliente);
        getSupportActionBar().setTitle("Manutenção de Clientes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        nome = findViewById(R.id.cadclientenome);
        telefone = findViewById(R.id.cadclientetelefone);
        obs = findViewById(R.id.cadclienteobservacao);
        botao_salvar = findViewById(R.id.cadclientebotaosalvar);
        preferencias = new Preferencias(this);


        Intent dados = getIntent();

        String acao = dados.getStringExtra("acao");

        if(acao.equals("1")){
            key_controle = "1";
            clienteLista = new ClienteLista();
            clienteDados = new ClienteDados();
        }else{
            key_controle = "2";
            clienteLista = (ClienteLista) dados.getSerializableExtra("obj");
            nome.setText(clienteLista.getNome());
            Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE_DADOS").child(clienteLista.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()) {
                        clienteDados = dataSnapshot.getValue(ClienteDados.class);
                        telefone.setText(clienteDados.getTelefone());
                        obs.setText(clienteDados.getObs());
                    }else{
                        clienteDados = new ClienteDados();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        botao_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nome.getText().toString().equals("")) {
                    Toast.makeText(CadClienteActivity.this, "Dados incompletos!", Toast.LENGTH_SHORT).show();
                } else {
                    if (key_controle.equals("1")) {
                        clienteLista.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE").push().getKey());
                        clienteLista.setNome(nome.getText().toString());
                        clienteDados.setKey(clienteLista.getKey());
                        clienteDados.setObs(obs.getText().toString());
                        clienteDados.setTelefone(telefone.getText().toString());
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE").child(clienteLista.getKey()).setValue(clienteLista);
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE_DADOS").child(clienteLista.getKey()).setValue(clienteDados);
                        Toast.makeText(CadClienteActivity.this, "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                        key_controle = "2";
                    } else {
                        clienteLista.setNome(nome.getText().toString());
                        clienteDados.setKey(clienteLista.getKey());
                        clienteDados.setTelefone(telefone.getText().toString());
                        clienteDados.setObs(obs.getText().toString());
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE").child(clienteLista.getKey()).setValue(clienteLista);
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE_DADOS").child(clienteLista.getKey()).setValue(clienteDados);
                        Toast.makeText(CadClienteActivity.this, "Salvo com sucesso!", Toast.LENGTH_LONG).show();
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
