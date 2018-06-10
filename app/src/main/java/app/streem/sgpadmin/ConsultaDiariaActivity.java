package app.streem.sgpadmin;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.streem.sgpadmin.Adapter.DiariaAdapter;
import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.ClienteLista;
import app.streem.sgpadmin.Model.Diaria;
import app.streem.sgpadmin.Model.Quarto;

public class ConsultaDiariaActivity extends AppCompatActivity {

    private Calendar myCalendarEntrada;
    private Calendar myCalendarSaida;
    private DatePickerDialog dataPickerEntrada;
    private DatePickerDialog dataPickerSaida;
    private List<Diaria> lista_despesas;
    private Preferencias preferencias;
    private Button botaobuscar;
    private TextView entrada;
    private TextView saida;
    private TextView valor;
    private ProgressBar progressBar;
    private TextView semregistro;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String inicio;
    private String fim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_diaria);
        getSupportActionBar().setTitle("Consultar Diárias");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        myCalendarEntrada = Calendar.getInstance();
        myCalendarSaida = Calendar.getInstance();
        entrada = findViewById(R.id.consultacaixadatainicial);
        saida = findViewById(R.id.consultacaixadatafinal);
        valor = findViewById(R.id.consultadiariatotal);
        botaobuscar = findViewById(R.id.consultacaixabotaobuscar);
        preferencias = new Preferencias(this);
        lista_despesas = new ArrayList<>();
        progressBar = findViewById(R.id.consultadiariaprogressBar);
        semregistro = findViewById(R.id.consultadiariasemregistro);
        recyclerView = findViewById(R.id.consultacaixarecyclerView);

        final DatePickerDialog.OnDateSetListener dateEntrada = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarEntrada.set(Calendar.YEAR, year);
                myCalendarEntrada.set(Calendar.MONTH, monthOfYear);
                myCalendarEntrada.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateDataEntrada();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        final DatePickerDialog.OnDateSetListener dateSaida = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarSaida.set(Calendar.YEAR, year);
                myCalendarSaida.set(Calendar.MONTH, monthOfYear);
                myCalendarSaida.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateDataSaida();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        entrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPickerEntrada = new DatePickerDialog(ConsultaDiariaActivity.this, dateEntrada, myCalendarEntrada
                        .get(Calendar.YEAR), myCalendarEntrada.get(Calendar.MONTH),
                        myCalendarEntrada.get(Calendar.DAY_OF_MONTH));
                dataPickerEntrada.show();
            }
        });

        saida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPickerSaida = new DatePickerDialog(ConsultaDiariaActivity.this, dateSaida, myCalendarSaida
                        .get(Calendar.YEAR), myCalendarSaida.get(Calendar.MONTH),
                        myCalendarSaida.get(Calendar.DAY_OF_MONTH));
                dataPickerSaida.show();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiariaAdapter(lista_despesas, this);

        recyclerView.setAdapter(adapter);

        botaobuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entrada.getText().toString().equals("") | saida.getText().toString().equals("Selecionar")){
                    Toast.makeText(ConsultaDiariaActivity.this, "Selecione as datas!",Toast.LENGTH_SHORT).show();
                }else {
                    getFirebase();
                }
            }
        });
    }

    public void getFirebase(){
        lista_despesas.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        semregistro.setVisibility(View.GONE);
        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("DIARIAS").orderByChild("dataloc").startAt(inicio).endAt(fim).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    final Diaria preco = dados.getValue(Diaria.class);

                    Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE").child(preco.getKey_cliente()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ClienteLista clienteLista = dataSnapshot.getValue(ClienteLista.class);
                            preco.setKey_cliente(clienteLista.getNome());
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("QUARTOS").child(preco.getKey_quarto()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Quarto quarto = dataSnapshot.getValue(Quarto.class);
                            preco.setKey_quarto(quarto.getNome());
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    lista_despesas.add(preco);
                    adapter.notifyDataSetChanged();
                }
                if(lista_despesas.size() > 0){
                    progressBar.setVisibility(View.GONE);
                    semregistro.setVisibility(View.GONE);
                    calcularTotal();
                    adapter.notifyDataSetChanged();
                }else {
                    progressBar.setVisibility(View.GONE);
                    semregistro.setVisibility(View.VISIBLE);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void calcularTotal(){
        BigDecimal tt = new BigDecimal("0.00");
        for(Diaria c : lista_despesas){
            tt = tt.add(new BigDecimal(c.getValor()));
        }
        valor.setText(tt.toString());
    }

    private void updateDataEntrada() throws ParseException {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        String dataEscolhida = sdf.format(myCalendarEntrada.getTime()); //transforma data em string
        entrada.setText(dataEscolhida);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date d = format.parse(dataEscolhida); //transforma data em tempo sem hora
        inicio = String.valueOf(d.getTime());
    }

    private void updateDataSaida() throws ParseException {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        String dataEscolhida = sdf.format(myCalendarSaida.getTime()); //transforma data em string
        saida.setText(dataEscolhida);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date d = format.parse(dataEscolhida); //transforma data em tempo sem hora
        fim = String.valueOf(d.getTime());
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
