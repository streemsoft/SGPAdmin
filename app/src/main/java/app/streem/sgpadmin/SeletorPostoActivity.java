package app.streem.sgpadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Login;

public class SeletorPostoActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView semregistros;
    private ListView listView;
    private Preferencias preferencias;
    private List<Login> recibos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seletor_posto);
        getSupportActionBar().setTitle("Selecionar Posto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        progressBar = findViewById(R.id.seletorfuncionarioprogressBar22);
        semregistros = findViewById(R.id.seletorfuncionariosemregistro2);
        listView = findViewById(R.id.seletorfuncionariolistview2);
        recibos = new ArrayList<>();
        preferencias = new Preferencias(this);
        getFirebase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preferencias.setCHAVE_KEY_POSTO_ATIVO(recibos.get(position).getKey_posto());
                preferencias.setCHAVE_KEY_POSTO_NOME(recibos.get(position).getNome());
                finish();
            }
        });

    }

    public void getFirebase(){
        recibos.clear();
        Firebase.getDatabaseReference().child("LOGIN").child(preferencias.getCHAVE_INDENTIFICADOR()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Login clienteLista = dados.getValue(Login.class);
                    recibos.add(clienteLista);
                }
                if(recibos.size() > 0){
                    ArrayAdapter<Login> adapter = new ArrayAdapter<Login>(SeletorPostoActivity.this,
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
