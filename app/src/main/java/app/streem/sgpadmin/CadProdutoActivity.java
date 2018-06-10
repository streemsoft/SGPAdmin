package app.streem.sgpadmin;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Produto;

public class CadProdutoActivity extends AppCompatActivity {

    private TextInputEditText nome;
    private TextInputEditText quantidade;
    private ImageView maisum;
    private ImageView menosum;
    private Button botao_salvar;
    private Preferencias preferencias;
    private String key_controle;
    private Produto produto;
    private TextInputEditText preco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_produto);
        getSupportActionBar().setTitle("Manutenção de Produtos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        nome = findViewById(R.id.cadprodutonome);
        quantidade = findViewById(R.id.cadprodutoquantidade);
        maisum = findViewById(R.id.cadprodutomaisum);
        menosum = findViewById(R.id.cadprodutomenosum);
        botao_salvar = findViewById(R.id.cadprodutosalvar);
        preferencias = new Preferencias(this);
        preco = findViewById(R.id.cadprodutopreco);

        Intent dados = getIntent();

        String acao = dados.getStringExtra("acao");

        if(acao.equals("1")){
            key_controle = "1";
            produto = new Produto();
            quantidade.setText("0");
        }else{
            key_controle = "2";
            produto = (Produto) dados.getSerializableExtra("obj");
            nome.setText(produto.getNome());
            quantidade.setText(produto.getQtd());
            preco.setText(produto.getPreco());
        }

        maisum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.valueOf(quantidade.getText().toString());
                temp++;
                quantidade.setText(String.valueOf(temp));
            }
        });

        menosum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.valueOf(quantidade.getText().toString());
                if(temp == 0){
                    Toast.makeText(CadProdutoActivity.this,"Produto sem estoque!",Toast.LENGTH_SHORT).show();
                }else {
                    temp--;
                    quantidade.setText(String.valueOf(temp));
                }
            }
        });

        botao_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nome.getText().toString().equals("") | quantidade.getText().toString().equals("") | preco.getText().toString().equals("")) {
                    Toast.makeText(CadProdutoActivity.this, "Dados incompletos!", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                    if (key_controle.equals("1")) {
                        produto.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRODUTOS").push().getKey());
                        produto.setNome(nome.getText().toString());

                        produto.setQtd(quantidade.getText().toString());
                        produto.setPreco(preco.getText().toString());
                        Date d = null; //transforma data em tempo sem hora
                        try {
                            d = format.parse(formato.format(new Date()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        produto.setUlt_compra(String.valueOf(d.getTime()));

                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRODUTOS").child(produto.getKey()).setValue(produto);
                        Toast.makeText(CadProdutoActivity.this, "Salvo com sucesso!", Toast.LENGTH_LONG).show();
                        key_controle = "2";
                    } else {
                        produto.setNome(nome.getText().toString());
                        produto.setQtd(quantidade.getText().toString());
                        produto.setPreco(preco.getText().toString());
                        Date d = null; //transforma data em tempo sem hora
                        try {
                            d = format.parse(formato.format(new Date()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        produto.setUlt_compra(String.valueOf(d.getTime()));
                        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("PRODUTOS").child(produto.getKey()).setValue(produto);
                        Toast.makeText(CadProdutoActivity.this, "Salvo com sucesso!", Toast.LENGTH_LONG).show();
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
