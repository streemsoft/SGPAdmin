package app.streem.sgpadmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.streem.sgpadmin.Adapter.BombaAdapter;
import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Bomba;
import app.streem.sgpadmin.Model.ContagensBomba;

public class FechamentoBombasActivity extends AppCompatActivity {

    private TextView totalLitros;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Button botaoFecharBomba;
    private Button botaoFecharCaixa;
    private EditText contagem;
    private Preferencias preferencias;
    private List<Bomba> bombaList;
    private ProgressBar progressBar;
    private TextView semregistro;
    private CardView cardView;
    private String keyselect;
    private String datahoje;
    private int controle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechamento_bombas);
        getSupportActionBar().setTitle("Fechamento de Bombas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        controle =1;
        keyselect = "0";
        totalLitros = findViewById(R.id.fechamentobombatotallitros);
        botaoFecharCaixa = findViewById(R.id.fechamentobombabotaofecharcaixa);
        recyclerView = findViewById(R.id.fechamentobombarecyclerview);
        contagem = findViewById(R.id.fechamentobombanovacontagem);
        botaoFecharBomba = findViewById(R.id.fechamentobombabotaofechar);
        progressBar = findViewById(R.id.fechamentobombaprogressbar);
        semregistro = findViewById(R.id.fechamentobombasemregistro);
        cardView = findViewById(R.id.fechamentobombacardview);

        preferencias = new Preferencias(this);
        bombaList = new ArrayList<>();

        botaoFecharBomba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!keyselect.equals("0")){
                    for(int i=0;i<bombaList.size();i++){
                        if(bombaList.get(i).getKey().equals(keyselect)){
                            bombaList.get(i).setCont_final(contagem.getText().toString());
                            bombaList.get(i).setStatus("2");
                            calculaLitros();
                            contagem.setText("");
                            cardView.setVisibility(View.GONE);
                            adapter.notifyItemChanged(i);
                        }
                    }
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(contagem.getWindowToken(), 0);

                }
            }
        });

        botaoFecharCaixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder confirm = new AlertDialog.Builder(FechamentoBombasActivity.this);
                confirm.setTitle("Confirmar Fechamento?");
                confirm.setCancelable(true);
                confirm.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                confirm.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(verificaBombas()){
                            preferencias.setCHAVE_KEY_TOTAL_LITROS(totalLitros.getText().toString());
                            BigDecimal totaldinheiro = new BigDecimal("0.00");
                            for (Bomba b : bombaList){
                                BigDecimal fi = new BigDecimal(b.getCont_final());
                                fi = fi.subtract(new BigDecimal(b.getContagem()));
                                b.setStatus("1");
                                b.setContagem(b.getCont_final());
                                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("BOMBAS").child(b.getKey()).setValue(b);
                                ContagensBomba contagensBomba = new ContagensBomba();
                                contagensBomba.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("BOMBAS_CONTAGENS").push().getKey());
                                contagensBomba.setDatacad(datahoje);
                                contagensBomba.setKey_bomba(b.getKey());
                                contagensBomba.setLitros(fi.toString());
                                BigDecimal preco = new BigDecimal(b.getPreco());
                                contagensBomba.setValor(fi.multiply(preco).toString());
                                totaldinheiro = totaldinheiro.add(fi.multiply(preco));
                                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("BOMBAS_CONTAGENS").child(contagensBomba.getKey()).setValue(contagensBomba);

                            }
                            preferencias.setCHAVE_KEY_TOTAL_DINHEIRO(totaldinheiro.toString());
                            Intent i = new Intent(FechamentoBombasActivity.this, LancamentosActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(FechamentoBombasActivity.this,"Fechamento incompleto!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog alertDialog = confirm.create();
                alertDialog.show();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BombaAdapter(bombaList, this,new BombaAdapter.OnItemClickListener() {
            @Override public void onItemClick(Bomba item) {
                keyselect = item.getKey();
                cardView.setVisibility(View.VISIBLE);
            }});

        recyclerView.setAdapter(adapter);
        getFirebase();

        try {
            setDatahoje();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void setDatahoje() throws ParseException {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        String dataEscolhida = sdf.format(new Date()); //transforma data em string
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date d = format.parse(dataEscolhida); //transforma data em tempo sem hora
        datahoje = String.valueOf(d.getTime());
    }

    public void calculaLitros(){
        BigDecimal total = new BigDecimal("0.00");
        for (Bomba b : bombaList){
            if(b.getStatus().equals("2")){
                BigDecimal fi = new BigDecimal(b.getCont_final());
                BigDecimal ini = new BigDecimal(b.getContagem());
                if(ini.compareTo(fi) > 0){
                    Toast.makeText(FechamentoBombasActivity.this,"Contagem inválida!", Toast.LENGTH_SHORT).show();
                    b.setStatus("1");
                    b.setCont_final(ini.toString());
                }else {
                    fi = fi.subtract(ini);
                    total = total.add(fi);
                    Toast.makeText(FechamentoBombasActivity.this,"Salvo com sucesso!",Toast.LENGTH_SHORT).show();
                }
            }
        }
        totalLitros.setText(total.toString());
    }

    public Boolean verificaBombas(){
        for (Bomba b : bombaList){
            if(b.getStatus().equals("1")){
                return false;
            }
        }
        return true;
    }

    public void getFirebase(){
        adapter.notifyDataSetChanged();
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("BOMBAS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Bomba preco = dados.getValue(Bomba.class);
                    bombaList.add(preco);
                    adapter.notifyDataSetChanged();
                }
                if(bombaList.size() > 0){
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
