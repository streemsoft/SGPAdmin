package app.streem.sgpadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.streem.sgpadmin.Adapter.ValesAdapter;
import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.ContaCorrente;
import app.streem.sgpadmin.Model.Recibo;
import app.streem.sgpadmin.Model.Vale;
import app.streem.sgpadmin.Model.ValeCliente;

public class PagValesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Vale> listaVales;
    private Preferencias preferencias;
    private ProgressBar progressBar;
    private TextView semregistro;
    private RadioGroup radioGroup;
    private EditText valor;
    private TextView total;
    private ImageView botaomaisvales;
    private Button botaosalvar;
    private String keycliente;
    private TextView selecionado;
    private int acao;
    private Vale valeSelecionado;
    private int qtd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_vales);
        getSupportActionBar().setTitle("Pagamento de Vales");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        listaVales = new ArrayList<>();
        recyclerView = findViewById(R.id.pagvalerecyclerView);
        preferencias = new Preferencias(this);
        progressBar = findViewById(R.id.pagvaleprogressBar);
        semregistro = findViewById(R.id.pagvalesemregistros);
        botaomaisvales = findViewById(R.id.pagvalemaisvales);
        radioGroup = findViewById(R.id.pagvaleradioGroup);
        valor = findViewById(R.id.pagvalesvalor);
        total = findViewById(R.id.pagvalestotal);
        botaosalvar = findViewById(R.id.pagvalesalvar);
        selecionado = findViewById(R.id.pagvaleselecionado);

        radioGroup.check(R.id.pagvalepagamento);
        acao = 1;
        qtd = 30;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.pagvalepagamento:
                        acao = 1;
                        break;
                    case R.id.pagvaledesconto:
                        acao = 2;
                        break;
                    case R.id.pagvaleacrescimo:
                        acao = 3;
                        break;
                }
            }
        });

        Intent dados = getIntent();

        keycliente = dados.getStringExtra("key");

        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selecionado.getText().toString().equals("Selecione um vale")){
                    Toast.makeText(PagValesActivity.this,"Selecione um vale!",Toast.LENGTH_SHORT).show();
                }else{
                    if(!valor.getText().toString().equals("")) {
                        switch (acao) {
                            case 1:
                                pagar();
                                break;
                            case 2:
                                desconto();
                                break;
                            case 3:
                                acrescimo();
                                break;
                        }
                    }else{
                        Toast.makeText(PagValesActivity.this,"Valor não informado!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        botaomaisvales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFirebaseGeral();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ValesAdapter(listaVales, this,new ValesAdapter.OnItemClickListener() {
            @Override public void onItemClick(Vale item) {
                valeSelecionado = item;
                selecionado.setText(valeSelecionado.getAtual());
            }});

        recyclerView.setAdapter(adapter);
        getFirebase();
    }

    public void acrescimo(){
        BigDecimal atual = new BigDecimal(valeSelecionado.getAtual());
        try {
                BigDecimal temp = new BigDecimal(valor.getText().toString());
                atual = atual.add(temp);
                valeSelecionado.setAtual(atual.toString());
                BigDecimal vlasc = new BigDecimal(valeSelecionado.getValor_acrecimo());
                vlasc = vlasc.add(temp);
                valeSelecionado.setValor_acrecimo(vlasc.toString());
                atualizaLista();
                totalizador();
                selecionado.setText("Selecione um vale");
                Toast.makeText(PagValesActivity.this,"Acréscimo realizado!",Toast.LENGTH_SHORT).show();
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").child(valeSelecionado.getKey()).setValue(valeSelecionado);
                valor.setText("");
            if(!valeSelecionado.getAtual().equals("0.00")){
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES_CLIENTES").child(keycliente).child(valeSelecionado.getKey()).child("situacao").setValue("1");
            }
        }catch (Exception e){
            Toast.makeText(PagValesActivity.this,"Verifique valor informado!",Toast.LENGTH_SHORT).show();
        }
    }

    public void desconto(){
        BigDecimal atual = new BigDecimal(valeSelecionado.getAtual());
        try {
            BigDecimal temp = new BigDecimal(valor.getText().toString());
            if(temp.compareTo(atual) > 0){
                Toast.makeText(PagValesActivity.this,"Valor incorreto!",Toast.LENGTH_SHORT).show();
            }else {
                atual = atual.subtract(temp);
                valeSelecionado.setAtual(atual.toString());
                BigDecimal vldesc = new BigDecimal(valeSelecionado.getValor_desconto());
                vldesc = vldesc.add(temp);
                valeSelecionado.setValor_desconto(vldesc.toString());
                atualizaLista();
                totalizador();
                selecionado.setText("Selecione um vale");
                Toast.makeText(PagValesActivity.this,"Desconto realizado!",Toast.LENGTH_SHORT).show();
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").child(valeSelecionado.getKey()).setValue(valeSelecionado);
                if(valeSelecionado.getAtual().equals("0.00")){
                    Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES_CLIENTES").child(keycliente).child(valeSelecionado.getKey()).child("situacao").setValue("2");
                }
                valor.setText("");
            }
        }catch (Exception e){
            Toast.makeText(PagValesActivity.this,"Verifique valor informado!",Toast.LENGTH_SHORT).show();
        }
    }

    public void pagar(){
        BigDecimal atual = new BigDecimal(valeSelecionado.getAtual());
        try {
            BigDecimal temp = new BigDecimal(valor.getText().toString());
            if(temp.compareTo(atual) > 0){
                Toast.makeText(PagValesActivity.this,"Valor incorreto!",Toast.LENGTH_SHORT).show();
            }else {
                atual = atual.subtract(temp);
                valeSelecionado.setAtual(atual.toString());
                BigDecimal vlpago = new BigDecimal(valeSelecionado.getValor_pago());
                vlpago = vlpago.add(temp);
                valeSelecionado.setValor_pago(vlpago.toString());
                atualizaLista();
                totalizador();
                selecionado.setText("Selecione um vale");
                Toast.makeText(PagValesActivity.this,"Pagamento realizado!",Toast.LENGTH_SHORT).show();
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").child(valeSelecionado.getKey()).setValue(valeSelecionado);
                if(valeSelecionado.getAtual().equals("0.00")){
                    Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES_CLIENTES").child(keycliente).child(valeSelecionado.getKey()).child("situacao").setValue("2");
                }

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
                String dataEscolhida = sdf.format(new Date()); //transforma data em string
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date d = format.parse(dataEscolhida); //transforma data em tempo sem hora

                Recibo recibo = new Recibo();
                recibo.setDatapag(String.valueOf(d.getTime()));
                recibo.setKey_cliente(valeSelecionado.getKey_cliente());
                recibo.setValor(valor.getText().toString());
                recibo.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("RECIBOS").push().getKey());
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("RECIBOS").child(recibo.getKey()).setValue(recibo);

                    ContaCorrente contaCorrente = new ContaCorrente();
                    contaCorrente.setDatacad(String.valueOf(d.getTime()));
                    contaCorrente.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CONTACORRENTE").push().getKey());
                    contaCorrente.setOrigem(valeSelecionado.getOrigem());
                    contaCorrente.setValor(temp.toString());
                    Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CONTACORRENTE").child(contaCorrente.getKey()).setValue(contaCorrente);

                valor.setText("");
            }
        }catch (Exception e){
            Toast.makeText(PagValesActivity.this,"Verifique valor informado!",Toast.LENGTH_SHORT).show();
        }
    }

    public void totalizador(){
        BigDecimal temp = new BigDecimal("0.00");
        for(Vale v : listaVales){
            temp = temp.add(new BigDecimal(v.getAtual()));
        }
        total.setText(temp.toString());
    }

    public void atualizaLista(){
        for(int i=0; i < listaVales.size() ; i++){
            if(listaVales.get(i).getKey().equals(valeSelecionado.getKey())){
                listaVales.set(i, valeSelecionado);
                adapter.notifyItemChanged(i);
            }
        }
    }

    public void getFirebase(){
        adapter.notifyDataSetChanged();
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES_CLIENTES").child(keycliente).orderByChild("situacao").equalTo("1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    ValeCliente preco = dados.getValue(ValeCliente.class);
                    Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").child(preco.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listaVales.add(dataSnapshot.getValue(Vale.class));
                            adapter.notifyDataSetChanged();
                            if(listaVales.size() > 0){
                                progressBar.setVisibility(View.GONE);
                                semregistro.setVisibility(View.GONE);
                                totalizador();
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
                if(listaVales.size() > 0){
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

    public void getFirebaseGeral(){
        listaVales.clear();
        adapter.notifyDataSetChanged();
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES_CLIENTES").child(keycliente).limitToLast(qtd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    ValeCliente preco = dados.getValue(ValeCliente.class);
                    Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").child(preco.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listaVales.add(dataSnapshot.getValue(Vale.class));
                            adapter.notifyDataSetChanged();
                            if(listaVales.size() > 0){
                                progressBar.setVisibility(View.GONE);
                                semregistro.setVisibility(View.GONE);
                                totalizador();
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
                if(listaVales.size() > 0){
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
        qtd = qtd + 30;
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
