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
import app.streem.sgpadmin.Model.ClienteLista;

public class ListaClientesGeralActivity extends AppCompatActivity {

    private ListView lista;
    private Preferencias preferencias;
    private ProgressBar progressBar;
    private TextView semregistros;
    private List<ClienteLista> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes_geral);

        getSupportActionBar().setTitle("Lista de Clientes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        lista = findViewById(R.id.listaclienteslistview);
        progressBar = findViewById(R.id.listaclientesprogressBar);
        semregistros = findViewById(R.id.listclientessemregistros);
        preferencias = new Preferencias(this);
        clientes = new ArrayList<>();

        getFirebase();

        Intent dados = getIntent();

        final String acao = dados.getStringExtra("acao");


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                switch (acao) {
                    case "1":
                        i = new Intent(ListaClientesGeralActivity.this, PagValesActivity.class);
                        i.putExtra("key", clientes.get(position).getKey());
                        //i.putExtra("acao", "2");
                        startActivity(i);
                        //finish();
                        break;
                    case "2":
                        i = new Intent(ListaClientesGeralActivity.this, RecibosActivity.class);
                        i.putExtra("key", clientes.get(position).getKey());
                        i.putExtra("nome", clientes.get(position).getNome());
                        startActivity(i);
                        //finish();
                        break;
                }
            }
        });
    }

    public void getFirebase(){
        clientes.clear();
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    ClienteLista clienteLista = dados.getValue(ClienteLista.class);
                    clientes.add(clienteLista);
                }
                if(clientes.size() > 0){
                    ArrayAdapter<ClienteLista> adapter = new ArrayAdapter<ClienteLista>(ListaClientesGeralActivity.this,
                            android.R.layout.simple_list_item_1,clientes);
                    lista.setAdapter(adapter);
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
