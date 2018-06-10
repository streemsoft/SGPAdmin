package app.streem.sgpadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Login;

public class LoginActivity extends AppCompatActivity {

    private Preferencias preferencias;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferencias = new Preferencias(this);

        if(preferencias.getCHAVE_INDENTIFICADOR().equals("1")) {

            final TextInputEditText email = findViewById(R.id.loginemail);
            final TextInputEditText senha = findViewById(R.id.loginsenha);
            progressBar = findViewById(R.id.loginprogressBar);
            Button entrar = findViewById(R.id.loginentrar);


            try {
                Firebase.loginDatabase();
            } catch (Exception e) {

            }

            entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailTexto = email.getText().toString();
                    String senhaTexto = senha.getText().toString();
                    if (emailTexto.isEmpty() | senhaTexto.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Email ou Senha inválido", Toast.LENGTH_LONG).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        validarLogin(emailTexto, senhaTexto);
                    }
                }
            });
        }else{
            Intent i;
            if(preferencias.getCHAVE_NOME_USUARIO().equals("1")){
                i = new Intent(LoginActivity.this,HomeGeralActivity.class);
                startActivity(i);
                finish();
            }else {
                i = new Intent(LoginActivity.this,HomeAdminActivity.class);
                startActivity(i);
                finish();
            }
        }

    }

    private void validarLogin(String email, String senna) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, senna).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    final FirebaseUser user = task.getResult().getUser();

                    Firebase.getDatabaseReference().child("LOGIN").child(user.getUid()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataS : dataSnapshot.getChildren()) {
                                Login login = dataS.getValue(Login.class);
                                preferencias.setCHAVE_KEY_POSTO_ATIVO(login.getKey_posto());
                                preferencias.setCHAVE_KEY_POSTO_NOME(login.getNome());
                            }
                            Firebase.getDatabaseReference().child("LOGIN_PER").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Intent i;
                                    preferencias.setUsuarioLogado(user.getUid(),dataSnapshot.getValue().toString());
                                    if(dataSnapshot.getValue().toString().equals("1")){
                                        i = new Intent(LoginActivity.this,HomeGeralActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else {
                                        i = new Intent(LoginActivity.this,HomeAdminActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                }else{
                    Toast.makeText(LoginActivity.this,"Usuário Inválido", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
