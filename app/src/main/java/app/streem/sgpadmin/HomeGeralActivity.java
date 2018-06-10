package app.streem.sgpadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Frentista;
import app.streem.sgpadmin.Model.Login;

public class HomeGeralActivity extends AppCompatActivity {

    private Preferencias preferencias;
    private TextView funci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_geral);

        CardView posto = findViewById(R.id.homepostoselecionado);
        CardView pousada = findViewById(R.id.homepousadaselecionada);
        TextView sair = findViewById(R.id.homegeralsair);
        funci = findViewById(R.id.homegeralfuncionariosel);
        ImageView alterafunci = findViewById(R.id.hometrocarfuncionario);

        preferencias = new Preferencias(this);

        funci.setText(preferencias.getCHAVE_KEY_FUNCIONARIO_NOME());

        alterafunci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeGeralActivity.this, SeletorFuncionarioActivity.class);
                startActivity(i);
            }
        });


        posto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(funci.getText().toString().equals("Selecionar!")){
                    Toast.makeText(HomeGeralActivity.this,"Cadastre ou Selecione um funcionário!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(HomeGeralActivity.this, FechamentoBombasActivity.class);
                    startActivity(i);
                }
            }
        });

        pousada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(funci.getText().toString().equals("Selecionar!")){
                    Toast.makeText(HomeGeralActivity.this,"Cadastre ou Selecione um funcionário!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent i = new Intent(HomeGeralActivity.this, DiariaActivity.class);
                    startActivity(i);
                }
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.logOut();
                preferencias.limpar();
                Intent i = new Intent(HomeGeralActivity.this,LoginActivity.class);
                //i.putExtra("acao","2");
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        funci.setText(preferencias.getCHAVE_KEY_FUNCIONARIO_NOME());
        getSupportActionBar().setTitle("SGP "+preferencias.getCHAVE_KEY_POSTO_NOME());
    }

}
