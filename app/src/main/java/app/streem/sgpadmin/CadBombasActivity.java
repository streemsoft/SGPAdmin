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
import app.streem.sgpadmin.Model.Bomba;

public class CadBombasActivity extends AppCompatActivity {

    private TextView semregistros;
    private TextInputEditText nome;
    private TextInputEditText contagem;
    private TextInputEditText preco;
    private Button botao_salvar;
    private ProgressBar progressBar;
    private ListView listView;
    private Preferencias preferencias;
    private List<Bomba> frentistas;
    private Bomba selecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_bombas);
        getSupportActionBar().setTitle("Manutenção de Bombas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        frentistas = new ArrayList<>();
        selecionado = new Bomba();
        selecionado.setKey("null");
        semregistros = findViewById(R.id.cadbombasemregistros);
        nome = findViewById(R.id.cadbombadescricao);
        botao_salvar = findViewById(R.id.cadbombabotaosalvar);
        progressBar = findViewById(R.id.cadbombaprogressBar);
        listView = findViewById(R.id.cadbombaslistview);
        preferencias = new Preferencias(this);
        contagem = findViewById(R.id.cadbombacontagem);
        preco = findViewById(R.id.cadbombapreco);
        getFirebase();

        botao_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nome.getText().toString().equals("") | contagem.getText().toString().equals("")) {
                    Toast.makeText(CadBombasActivity.this,"Dados incompletos!",Toast.LENGTH_SHORT).show();
                } else {
                    if (selecionado.getKey().equals("null")) {
                        selecionado.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("BOMBAS").push().getKey());
                        selecionado.setNome(nome.getText().toString());
                        selecionado.setContagem(contagem.getText().toString());
                        selecionado.setCont_final(contagem.getText().toString());
                        selecionado.setPreco(preco.getText().toString());
                        selecionado.setStatus("1");
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("BOMBAS").child(selecionado.getKey()).setValue(selecionado).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                selecionado = new Bomba();
                                selecionado.setKey("null");
                                nome.setText("");
                                contagem.setText("");
                                preco.setText("");
                                getFirebase();
                                Toast.makeText(CadBombasActivity.this,"Salvo com sucesso!",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        selecionado.setNome(nome.getText().toString());
                        selecionado.setNome(nome.getText().toString());
                        selecionado.setContagem(contagem.getText().toString());
                        selecionado.setCont_final(contagem.getText().toString());
                        selecionado.setPreco(preco.getText().toString());
                        selecionado.setStatus("1");
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("BOMBAS").child(selecionado.getKey()).setValue(selecionado).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                selecionado = new Bomba();
                                selecionado.setKey("null");
                                nome.setText("");
                                contagem.setText("");
                                preco.setText("");
                                getFirebase();
                                Toast.makeText(CadBombasActivity.this,"Salvo com sucesso!",Toast.LENGTH_SHORT).show();
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
                contagem.setText(selecionado.getContagem());
                preco.setText(selecionado.getPreco());
            }
        });
    }

    public void getFirebase(){
        frentistas.clear();
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("BOMBAS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Bomba clienteLista = dados.getValue(Bomba.class);
                    frentistas.add(clienteLista);
                }
                if(frentistas.size() > 0){
                    ArrayAdapter<Bomba> adapter = new ArrayAdapter<Bomba>(CadBombasActivity.this,
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
