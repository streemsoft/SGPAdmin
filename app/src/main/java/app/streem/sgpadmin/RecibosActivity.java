package app.streem.sgpadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Recibo;

public class RecibosActivity extends AppCompatActivity {

    private TextView semregistros;
    private TextView nometela;
    private ProgressBar progressBar;
    private ListView listView;
    private Preferencias preferencias;
    private List<Recibo> recibos;
    private String key;
    private ImageView botao;
    private int qtd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibos);
        getSupportActionBar().setTitle("Recibos de Pagamentos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        Intent dados = getIntent();

        String nome = dados.getStringExtra("nome");
        key = dados.getStringExtra("key");

        recibos = new ArrayList<>();
        semregistros = findViewById(R.id.recibossemregistros);
        nometela = findViewById(R.id.recibosnometela);
        progressBar = findViewById(R.id.recibosprogressBar);
        listView = findViewById(R.id.reciboslistview);
        preferencias = new Preferencias(this);
        botao = findViewById(R.id.recibosbotao);
        qtd = 20;

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qtd = qtd + 10;
                getFirebase();
                Toast.makeText(RecibosActivity.this,"+10 Recibos...",Toast.LENGTH_SHORT).show();
            }
        });

        nometela.setText(nome);
        getFirebase();

    }

    public void getFirebase(){
        recibos.clear();
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("RECIBOS").orderByChild("key_cliente").equalTo(key).limitToLast(qtd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Recibo clienteLista = dados.getValue(Recibo.class);
                    recibos.add(clienteLista);
                }
                if(recibos.size() > 0){
                    Collections.reverse(recibos);
                    ArrayAdapter<Recibo> adapter = new ArrayAdapter<Recibo>(RecibosActivity.this,
                            android.R.layout.simple_list_item_1,recibos);
                    listView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                    semregistros.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    semregistros.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
