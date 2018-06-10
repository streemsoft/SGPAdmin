package app.streem.sgpadmin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import app.streem.sgpadmin.Adapter.LancamentoAdapter;
import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.ClienteLista;
import app.streem.sgpadmin.Model.ContaCorrente;
import app.streem.sgpadmin.Model.FechamentoCaixa;
import app.streem.sgpadmin.Model.Lancamento;
import app.streem.sgpadmin.Model.Preco;
import app.streem.sgpadmin.Model.Produto;
import app.streem.sgpadmin.Model.Vale;
import app.streem.sgpadmin.Model.ValeCliente;

public class LancamentosActivity extends AppCompatActivity {
    //-----------------------------
    private TextView caixafinal;
    private TextView totaldespesas;
    private TextView totalprodutos;
    private TextView totaldinheiro;
    private TextView totalcartao;
    private TextView totalvale;
    private TextView totaldototal;
    //-----------------------------
    private ConstraintLayout abastecimentolayout;
    private ConstraintLayout produtoslayout;
    private ConstraintLayout despesaslayout;
    //-----------------------------
    private Button salvarabastecimento;
    private Button salvarproduto;
    private Button salvardespesa;
    private Button salvarcaixa;
    //-----------------------------
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Spinner clientes_spinner;
    private Spinner produtos_spinner;
    //private Spinner combustivel_spinner;
    //-----------------------------
    private RadioGroup tipolancamento;
    private RadioGroup fpagproduto;
    private RadioGroup fpagabastecimento;

    //------------------------------
    private List<Lancamento> lancamentos_final;
    private List<Lancamento> lancamentos_exibir;
    private List<ClienteLista> clienteList;
    private List<Produto> produtoList;
    private List<Preco> precosList;
    //-------------------------------

    private int escolhaPagAbastecimento;
    private int escolhaPagProduto;
    private int getProdutosControle;

    private Preferencias preferencias;

    //--------------------------------
    private EditText valorAbastecido;
    private EditText quantidadeProdutos;
    private EditText valorDespesa;
    private TextInputEditText motivoDespesa;

    private String dataHoje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamentos);
        getSupportActionBar().setTitle("Laçamentos de Caixa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        getProdutosControle = 1;

        instancia();
        setPagamentos();
        carregarFiltrosFirebase();

        try {
            botoes();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LancamentoAdapter(lancamentos_exibir, this,new LancamentoAdapter.OnItemClickListener() {
            @Override public void onItemClick(Lancamento item) {
                remover(item.getKey());
            }});

        recyclerView.setAdapter(adapter);

        totalprodutos.setText(preferencias.getCHAVE_KEY_TOTAL_DINHEIRO());


    }

    public void botoes() throws ParseException {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        String dataEscolhida = sdf.format(new Date()); //transforma data em string
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date d = format.parse(dataEscolhida); //transforma data em tempo sem hora
        dataHoje = String.valueOf(d.getTime());

        salvarcaixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(LancamentosActivity.this);
                confirm.setTitle("Confirmar Fechamento de Caixa?");
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
                        try {
                            fecharCaixa();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

                AlertDialog alertDialog = confirm.create();
                alertDialog.show();

            }
        });

        salvarabastecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lancamento lancamento = new Lancamento();
                lancamento.setDatacad(dataHoje);
                lancamento.setFpag(escolhaPagAbastecimento);
                lancamento.setFuncionario(clienteList.get(clientes_spinner.getSelectedItemPosition()).getKey());
                lancamento.setTipo(1);
                lancamento.setValor(valorAbastecido.getText().toString());
                lancamento.setKey(String.valueOf(new Date().getTime()+ Math.round(1)));
                //BigDecimal litros = new BigDecimal(valorAbastecido.getText().toString());
                //litros = litros.divide(new BigDecimal(precosList.get(combustivel_spinner.getSelectedItemPosition()).getValor()),2,RoundingMode.HALF_UP);
                //lancamento.setLitro_qtd(litros.toString());
                lancamento.setLitro_qtd("");
                lancamentos_final.add(lancamento);
                //------------------------------------------
                Lancamento lancamento2 = new Lancamento();
                lancamento2.setDatacad(dataHoje);
                lancamento2.setFpag(escolhaPagAbastecimento);
                lancamento2.setFuncionario(clienteList.get(clientes_spinner.getSelectedItemPosition()).getNome());
                lancamento2.setTipo(1);
                lancamento2.setValor(valorAbastecido.getText().toString());
                lancamento2.setKey(lancamento.getKey());
                //BigDecimal litros2 = new BigDecimal(valorAbastecido.getText().toString());
                //litros2 = litros2.divide(new BigDecimal(precosList.get(combustivel_spinner.getSelectedItemPosition()).getValor()),2,RoundingMode.HALF_UP);
                //lancamento2.setLitro_qtd(litros2.toString());
                lancamento2.setLitro_qtd("");
                lancamentos_exibir.add(lancamento2);
                adapter.notifyDataSetChanged();

                tipolancamento.clearCheck();
                abastecimentolayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                valorAbastecido.setText("");
                calcularTotal();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(valorAbastecido.getWindowToken(), 0);
            }
        });

        salvarproduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lancamento lancamento = new Lancamento();
                lancamento.setDatacad(dataHoje);
                lancamento.setFpag(escolhaPagProduto);
                lancamento.setFuncionario(clienteList.get(clientes_spinner.getSelectedItemPosition()).getKey());
                lancamento.setTipo(2);
                lancamento.setKey(String.valueOf(new Date().getTime()+ Math.round(1)));
                BigDecimal litros = new BigDecimal(produtoList.get(produtos_spinner.getSelectedItemPosition()).getPreco());
                litros = litros.multiply(new BigDecimal(quantidadeProdutos.getText().toString()));
                lancamento.setValor(litros.toString());
                lancamento.setLitro_qtd(quantidadeProdutos.getText().toString());
                lancamentos_final.add(lancamento);
                //------------------------------------------
                Lancamento lancamento2 = new Lancamento();
                lancamento2.setDatacad(dataHoje);
                lancamento2.setFpag(escolhaPagProduto);
                lancamento2.setFuncionario(clienteList.get(clientes_spinner.getSelectedItemPosition()).getNome());
                lancamento2.setTipo(2);
                lancamento2.setKey(lancamento.getKey());
                BigDecimal litros2 = new BigDecimal(produtoList.get(produtos_spinner.getSelectedItemPosition()).getPreco());
                litros2 = litros2.multiply(new BigDecimal(quantidadeProdutos.getText().toString()));
                lancamento2.setValor(litros2.toString());
                lancamento2.setLitro_qtd(quantidadeProdutos.getText().toString());
                lancamentos_exibir.add(lancamento2);
                adapter.notifyDataSetChanged();

                tipolancamento.clearCheck();
                produtoslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                quantidadeProdutos.setText("");
                calcularTotal();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(quantidadeProdutos.getWindowToken(), 0);
            }
        });

        salvardespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lancamento lancamento = new Lancamento();
                lancamento.setDatacad(dataHoje);
                lancamento.setFpag(1);
                lancamento.setFuncionario(preferencias.getCHAVE_KEY_FUNCIONARIO_NOME());
                lancamento.setTipo(3);
                lancamento.setValor(valorDespesa.getText().toString());
                lancamento.setKey(String.valueOf(new Date().getTime()+ Math.round(1)));
                lancamento.setLitro_qtd(motivoDespesa.getText().toString());
                lancamentos_final.add(lancamento);
                //------------------------------------------
                Lancamento lancamento2 = new Lancamento();
                lancamento2.setDatacad(dataHoje);
                lancamento2.setFpag(1);
                lancamento2.setFuncionario(motivoDespesa.getText().toString());
                lancamento2.setTipo(3);
                lancamento2.setValor(valorDespesa.getText().toString());
                lancamento2.setKey(String.valueOf(new Date().getTime()+ Math.round(1)));
                lancamento2.setLitro_qtd(motivoDespesa.getText().toString());
                lancamentos_exibir.add(lancamento2);
                adapter.notifyDataSetChanged();

                tipolancamento.clearCheck();
                despesaslayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                valorDespesa.setText("");
                motivoDespesa.setText("");
                calcularTotal();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(motivoDespesa.getWindowToken(), 0);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(valorDespesa.getWindowToken(), 0);
            }
        });
    }


    public void fecharCaixa() throws ParseException {
        for (int i =0;i< lancamentos_final.size();i++){
            if( lancamentos_final.get(i).getFpag() == 3){
                Vale vale = new Vale();
                vale.setValor_acrecimo("0.00");
                vale.setValor_desconto("0.00");
                vale.setValor_pago("0.00");
                vale.setKey_cliente(lancamentos_final.get(i).getFuncionario());
                vale.setStatus("1");
                vale.setAtual(lancamentos_final.get(i).getValor());
                vale.setValor_total(lancamentos_final.get(i).getValor());
                switch (lancamentos_final.get(i).getTipo()){
                    case 1:
                        vale.setOrigem("Abastecimento");
                        break;
                    case 2:
                        vale.setOrigem("Produto");
                        break;
                }
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
                String dataEscolhida = sdf.format(new Date()); //transforma data em string
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date d = format.parse(dataEscolhida); //transforma data em tempo sem hora
                vale.setData(String.valueOf(d.getTime()));
                vale.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").push().getKey());
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").child(vale.getKey()).setValue(vale);
                ValeCliente valeCliente = new ValeCliente();
                valeCliente.setKey(vale.getKey());
                valeCliente.setSituacao("1");
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES_CLIENTES").child(vale.getKey_cliente()).child(vale.getKey()).setValue(valeCliente);
            }

            if (lancamentos_final.get(i).getTipo() == 2){
                Produto produto = produtoList.get(produtos_spinner.getSelectedItemPosition());
                produto.setUlt_venda(dataHoje);
                produto.setQtd(String.valueOf(new BigDecimal(produto.getQtd()).subtract(new BigDecimal(lancamentos_final.get(i).getLitro_qtd())).intValue()));
                produtoList.set(produtos_spinner.getSelectedItemPosition(),produto);

                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRODUTOS").child(produto.getKey()).setValue(produto);
            }

            if (lancamentos_final.get(i).getTipo() == 3){
                String key = Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("DESPESAS").push().getKey();
                lancamentos_final.get(i).setKey(key);
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("DESPESAS").child(key).setValue(lancamentos_final.get(i));
            }


        }
        FechamentoCaixa caixa = new FechamentoCaixa();
        caixa.setCartao(totalcartao.getText().toString());
        caixa.setDatacad(dataHoje);
        caixa.setDespesas(totaldespesas.getText().toString());
        caixa.setDinheiro(totaldinheiro.getText().toString());
        caixa.setVale(totalvale.getText().toString());
        caixa.setProdutos(totalprodutos.getText().toString());
        caixa.setFuncionario(preferencias.getCHAVE_KEY_FUNCIONARIO_NOME());
        caixa.setTotal(totaldototal.getText().toString());
        caixa.setLitros(preferencias.getCHAVE_KEY_TOTAL_LITROS());
        caixa.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("FECHAMENTO_CAIXA").push().getKey());
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("FECHAMENTO_CAIXA").child(caixa.getKey()).setValue(caixa);
        preferencias.setCHAVE_KEY_TOTAL_LITROS("0");

        if(!totaldinheiro.getText().toString().equals("0.00")){
            ContaCorrente contaCorrente = new ContaCorrente();
            contaCorrente.setDatacad(dataHoje);
            contaCorrente.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CONTACORRENTE").push().getKey());
            contaCorrente.setOrigem("Posto" +
                    "");
            contaCorrente.setValor(caixafinal.getText().toString());
            Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CONTACORRENTE").child(contaCorrente.getKey()).setValue(contaCorrente);
        }

        for(Lancamento x : lancamentos_final){
            if(x.getTipo() != 3){
                x.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("FECHAMENTO_DETAIL").child(dataHoje).child(caixa.getKey()).push().getKey());
                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("FECHAMENTO_DETAIL").child(dataHoje).child(caixa.getKey()).child(x.getKey()).setValue(x);
            }
        }

        Toast.makeText(LancamentosActivity.this,"Salvo com sucesso!",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void calcularTotal(){
        BigDecimal total = new BigDecimal("0.00");
        BigDecimal dinheiro = new BigDecimal("0.00");
        BigDecimal cartao = new BigDecimal("0.00");
        BigDecimal vale = new BigDecimal("0.00");
        int produtos = 0;
        BigDecimal despesas = new BigDecimal("0.00");

        for (Lancamento x : lancamentos_final){
            switch (x.getTipo()){
                case 1:
                    switch (x.getFpag()){
                        case 1:
                            dinheiro = dinheiro.add(new BigDecimal(x.getValor()));
                            break;
                        case 2:
                            cartao = cartao.add(new BigDecimal(x.getValor()));
                            break;
                        case 3:
                            vale = vale.add(new BigDecimal(x.getValor()));
                            break;
                    }
                    break;
                case 2:
                    switch (x.getFpag()){
                        case 1:
                            dinheiro = dinheiro.add(new BigDecimal(x.getValor()));
                            break;
                        case 2:
                            cartao = cartao.add(new BigDecimal(x.getValor()));
                            break;
                        case 3:
                            vale = vale.add(new BigDecimal(x.getValor()));
                            break;
                    }
                    produtos++;
                    break;
                case 3:
                    despesas = despesas.add(new BigDecimal(x.getValor()));
                    break;

            }
        }
        total = dinheiro.subtract(despesas);
        caixafinal.setText(total.toString());
        totalvale.setText(vale.toString());
        //totalprodutos.setText(String.valueOf(produtos));
        totaldinheiro.setText(dinheiro.toString());
        totaldespesas.setText(despesas.toString());
        totalcartao.setText(cartao.toString());
        BigDecimal finalcaixa = dinheiro.add(cartao).add(vale);
        totaldototal.setText(finalcaixa.toString());
    }

    public void remover(String key){
        for (int i = 0 ; i < lancamentos_exibir.size() ; i++){
            if(lancamentos_exibir.get(i).getKey().equals(key)){
                lancamentos_exibir.remove(i);
                lancamentos_final.remove(i);
                adapter.notifyItemRemoved(i);
            }
        }
        calcularTotal();
    }

    public void carregarFiltrosFirebase(){
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    ClienteLista quarto = dados.getValue(ClienteLista.class);
                    clienteList.add(quarto);
                }
                if(clienteList.size() > 0){
                    ArrayAdapter<ClienteLista> adapter = new ArrayAdapter<ClienteLista>(LancamentosActivity.this,
                            android.R.layout.simple_list_item_1,clienteList);
                    clientes_spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRECOS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Preco quarto = dados.getValue(Preco.class);
                    precosList.add(quarto);
                }
                if(precosList.size() > 0){
                    ArrayAdapter<Preco> adapter = new ArrayAdapter<Preco>(LancamentosActivity.this,
                            android.R.layout.simple_list_item_1,precosList);
                    combustivel_spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    public void carregarProdutos(){
        if(getProdutosControle == 1) {
            Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRODUTOS").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        Produto preco = dados.getValue(Produto.class);
                        produtoList.add(preco);
                        ArrayAdapter<Produto> adapter = new ArrayAdapter<Produto>(LancamentosActivity.this,
                                android.R.layout.simple_list_item_1, produtoList);
                        produtos_spinner.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        getProdutosControle = 2;

    }

    public void setPagamentos(){
        escolhaPagAbastecimento = 1;
        escolhaPagProduto = 1;
        fpagproduto.check(R.id.radioButtonproddinheiro);
        fpagabastecimento.check(R.id.radioButtoncombdinheiro);


        fpagabastecimento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtoncombdinheiro:
                        escolhaPagAbastecimento = 1;
                        break;
                    case R.id.radioButtoncombcartao:
                        escolhaPagAbastecimento = 2;
                        break;
                    case R.id.radioButtoncombvale:
                        escolhaPagAbastecimento = 3;
                        break;
                }
            }
        });

        fpagproduto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonproddinheiro:
                        escolhaPagProduto = 1;
                        break;
                    case R.id.radioButtonprodcartao:
                        escolhaPagProduto = 2;
                        break;
                    case R.id.radioButtonprodvale:
                        escolhaPagProduto = 3;
                        break;
                }
            }
        });

        tipolancamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonabastecimento:
                        abastecimentolayout.setVisibility(View.VISIBLE);
                        produtoslayout.setVisibility(View.GONE);
                        despesaslayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        break;
                    case R.id.radioButtonproduto:
                        abastecimentolayout.setVisibility(View.GONE);
                        produtoslayout.setVisibility(View.VISIBLE);
                        despesaslayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        carregarProdutos();
                        break;
                    case R.id.radioButtondespesa:
                        abastecimentolayout.setVisibility(View.GONE);
                        produtoslayout.setVisibility(View.GONE);
                        despesaslayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    public void instancia(){
        caixafinal = findViewById(R.id.lanccaixafinal);
        totaldespesas = findViewById(R.id.lancdespesas);
        totalprodutos = findViewById(R.id.lancprodutos);
        totaldinheiro = findViewById(R.id.lancdinheiro);
        totalcartao = findViewById(R.id.lanccartao);
        totalvale = findViewById(R.id.lancvales);

        abastecimentolayout = findViewById(R.id.conteinerabastecimento);
        produtoslayout = findViewById(R.id.conteinerproduto);
        despesaslayout = findViewById(R.id.conteinerdespesa);

        salvarabastecimento = findViewById(R.id.lancbuttoncombsalvar);
        salvarproduto = findViewById(R.id.buttonprodsalvar);
        salvardespesa = findViewById(R.id.buttondespesasalvar);
        salvarcaixa = findViewById(R.id.lancamentobotaofechar);

        tipolancamento = findViewById(R.id.radioGrouptipolancamento);
        fpagabastecimento = findViewById(R.id.radiogrupofpagcombustivel);
        fpagproduto = findViewById(R.id.radiogrupoproduto);

        lancamentos_exibir = new ArrayList<>();
        lancamentos_final = new ArrayList<>();
        clienteList = new ArrayList<>();
        produtoList = new ArrayList<>();
        precosList = new ArrayList<>();
        produtoList = new ArrayList<>();

        clientes_spinner = findViewById(R.id.spinnercliente);
        produtos_spinner = findViewById(R.id.spinnerproduto);
        //combustivel_spinner = findViewById(R.id.spinnercombustivel);

        recyclerView = findViewById(R.id.lancrecyclerview);
        lancamentos_final = new ArrayList<>();
        lancamentos_exibir = new ArrayList<>();
        preferencias = new Preferencias(this);

        valorAbastecido = findViewById(R.id.combvalor);
        quantidadeProdutos = findViewById(R.id.quantidadeprodlanc);
        motivoDespesa = findViewById(R.id.despesamotivo);
        valorDespesa = findViewById(R.id.despesavalor);
        totaldototal = findViewById(R.id.totaldototal);
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
        super.onBackPressed();
        Toast.makeText(LancamentosActivity.this, "Atenção os dados serão perdidos!",Toast.LENGTH_SHORT).show();
    }
}
