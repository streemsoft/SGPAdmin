package app.streem.sgpadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Login;

public class HomeAdminActivity extends AppCompatActivity {

    private Preferencias preferencias;
    private TextView nomeposto;
    private ImageView trocaposto;
    private TextView cadclient;
    private TextView manutencaoprodutos;
    private TextView pagamentoVales;
    private TextView registroPag;
    private TextView consultaCaixa;
    private TextView consultaDespesa;
    private TextView manuFuncionario;
    private TextView cadBombas;
    private TextView cadQuartos;
    private TextView consultaDiaria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        nomeposto = findViewById(R.id.homeadminpostoativo);
        trocaposto = findViewById(R.id.hometrocarposto);
        cadclient = findViewById(R.id.admincadclientes);
        manutencaoprodutos = findViewById(R.id.adminmanuproduto);
        pagamentoVales = findViewById(R.id.adminpagvales);
        registroPag = findViewById(R.id.adminregistropag);
        consultaCaixa = findViewById(R.id.adminconsultacaixa);
        consultaDespesa = findViewById(R.id.adminconsultadespesas);
        manuFuncionario = findViewById(R.id.adminmanufuncionarios);
        cadBombas = findViewById(R.id.admincadbomba);
        cadQuartos = findViewById(R.id.admincadquartos);
        consultaDiaria = findViewById(R.id.adminconsultadiarias);


        preferencias = new Preferencias(this);

        trocaposto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,SeletorPostoActivity.class);
                startActivity(i);
            }
        });

        cadclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,ListaClientesAdminActivity.class);
                i.putExtra("acao","1");
                startActivity(i);
            }
        });

        manutencaoprodutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,ListaProdutosActivity.class);
                //i.putExtra("acao","1");
                startActivity(i);
            }
        });

        pagamentoVales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,ListaClientesGeralActivity.class);
                i.putExtra("acao","1");
                startActivity(i);
            }
        });

        registroPag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,ListaClientesGeralActivity.class);
                i.putExtra("acao","2");
                startActivity(i);
            }
        });

        consultaCaixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,ConsultaCaixaActivity.class);
                //i.putExtra("acao","2");
                startActivity(i);
            }
        });

        consultaDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,ConsultaDespesaActivity.class);
                //i.putExtra("acao","2");
                startActivity(i);
            }
        });

        manuFuncionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,ManuFrentistaActivity.class);
                //i.putExtra("acao","2");
                startActivity(i);
            }
        });

        cadBombas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,CadBombasActivity.class);
                //i.putExtra("acao","2");
                startActivity(i);
            }
        });

        cadQuartos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,CadQuartosActivity.class);
                //i.putExtra("acao","2");
                startActivity(i);
            }
        });

        consultaDiaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this,ConsultaDiariaActivity.class);
                //i.putExtra("acao","2");
                startActivity(i);
            }
        });

        TextView sair = findViewById(R.id.adminsair);

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.logOut();
                preferencias.limpar();
                Intent i = new Intent(HomeAdminActivity.this,LoginActivity.class);
                //i.putExtra("acao","2");
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        nomeposto.setText(preferencias.getCHAVE_KEY_POSTO_NOME());
        getSupportActionBar().setTitle("SGP Administrativo");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
