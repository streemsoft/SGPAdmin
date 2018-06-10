package app.streem.sgpadmin;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Frentista;

public class ManuFrentistaActivity extends AppCompatActivity {

    private TextView semregistros;
    private TextInputEditText nome;
    private Button botao_salvar;
    private ProgressBar progressBar;
    private ListView listView;
    private Preferencias preferencias;
    private List<Frentista> frentistas;
    private Frentista selecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu_frentista);
        getSupportActionBar().setTitle("Manutenção de Frentistas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        frentistas = new ArrayList<>();
        selecionado = new Frentista();
        selecionado.setKey("null");
        semregistros = findViewById(R.id.manufrentistasemregistrp);
        nome = findViewById(R.id.manufrentistanome);
        botao_salvar = findViewById(R.id.cadquartosbotaosalvar);
        progressBar = findViewById(R.id.manufrentistaprogressbar);
        listView = findViewById(R.id.cadquartoslistview);
        preferencias = new Preferencias(this);
        getFirebase();

        botao_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nome.getText().toString().equals("")) {
                    Toast.makeText(ManuFrentistaActivity.this,"Dados incompletos!",Toast.LENGTH_SHORT).show();
                } else {
                    if (selecionado.getKey().equals("null")) {
                        selecionado.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("FRENTISTAS").push().getKey());
                        selecionado.setNome(nome.getText().toString());
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("FRENTISTAS").child(selecionado.getKey()).setValue(selecionado).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                selecionado = new Frentista();
                                selecionado.setKey("null");
                                nome.setText("");
                                getFirebase();
                                Toast.makeText(ManuFrentistaActivity.this,"Salvo com sucesso!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        selecionado.setNome(nome.getText().toString());
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("FRENTISTAS").child(selecionado.getKey()).setValue(selecionado).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                selecionado = new Frentista();
                                selecionado.setKey("null");
                                nome.setText("");
                                getFirebase();
                                Toast.makeText(ManuFrentistaActivity.this,"Salvo com sucesso!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecionado = frentistas.get(position);
                nome.setText(selecionado.getNome());
            }
        });

    }

    public void getFirebase(){
        frentistas.clear();
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("FRENTISTAS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Frentista clienteLista = dados.getValue(Frentista.class);
                    frentistas.add(clienteLista);
                }
                if(frentistas.size() > 0){
                    ArrayAdapter<Frentista> adapter = new ArrayAdapter<Frentista>(ManuFrentistaActivity.this,
                            android.R.layout.simple_list_item_1,frentistas);
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
