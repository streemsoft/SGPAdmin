package app.streem.sgpadmin;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.streem.sgpadmin.Adapter.PrecoAdapter;
import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Preco;

public class ListaPrecosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Preco> listaPrecos;
    private Preferencias preferencias;
    private ProgressBar progressBar;
    private TextView semregistro;
    private FloatingActionButton botaonovo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_precos);
        getSupportActionBar().setTitle("Manutenção de Preços");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        listaPrecos = new ArrayList<>();
        recyclerView = findViewById(R.id.listaprecorecyclerview);
        preferencias = new Preferencias(this);
        progressBar = findViewById(R.id.listaprecoprogressBar);
        semregistro = findViewById(R.id.listaprecosemregistro);
        botaonovo = findViewById(R.id.listaprecobotaonovo);

        botaonovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaPrecosActivity.this, CadPrecosActivity.class);
                intent.putExtra("acao","1");
                startActivity(intent);
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PrecoAdapter(listaPrecos, this);

        recyclerView.setAdapter(adapter);
        //getFirebase();
    }

    public void getFirebase(){
        listaPrecos.clear();
        adapter.notifyDataSetChanged();
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRECOS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Preco preco = dados.getValue(Preco.class);
                    listaPrecos.add(preco);
                    adapter.notifyDataSetChanged();
                }
                if(listaPrecos.size() > 0){
                    progressBar.setVisibility(View.GONE);
                    semregistro.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.GONE);
                    semregistro.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFirebase();
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
