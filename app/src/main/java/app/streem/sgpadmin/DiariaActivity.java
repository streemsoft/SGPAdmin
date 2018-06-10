package app.streem.sgpadmin;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.streem.sgpadmin.DAO.Firebase;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.ClienteLista;
import app.streem.sgpadmin.Model.ContaCorrente;
import app.streem.sgpadmin.Model.Diaria;
import app.streem.sgpadmin.Model.Quarto;
import app.streem.sgpadmin.Model.Vale;
import app.streem.sgpadmin.Model.ValeCliente;

public class DiariaActivity extends AppCompatActivity {

    private Spinner clientes;
    private Spinner quartos;
    private TextView entrada;
    private TextView saida;
    private EditText valor;
    private RadioGroup fpag;
    private Button botaosalvar;
    private List<ClienteLista> lista_clientes;
    private List<Quarto> lista_quartos;
    private Preferencias preferencias;
    private Diaria diaria;
    private Calendar myCalendarEntrada;
    private Calendar myCalendarSaida;
    private DatePickerDialog dataPickerEntrada;
    private DatePickerDialog dataPickerSaida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaria);
        getSupportActionBar().setTitle("Lançamento de Diárias");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão

        myCalendarEntrada = Calendar.getInstance();
        myCalendarSaida = Calendar.getInstance();
        diaria = new Diaria();
        clientes = findViewById(R.id.diariaspinnercliente);
        quartos = findViewById(R.id.diariaspinnerquarto);
        entrada = findViewById(R.id.diaraidataentrada);
        saida = findViewById(R.id.diariadatasaida);
        valor = findViewById(R.id.diariavalorpago);
        fpag = findViewById(R.id.diariaradiogrupo);
        botaosalvar = findViewById(R.id.diariabotaosalvar);
        preferencias = new Preferencias(this);
        lista_clientes = new ArrayList<>();
        lista_quartos = new ArrayList<>();

        //seleciona dinheiro
        fpag.check(R.id.diariaradioButtondinheiro);
        diaria.setFpag("1");

        fpag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.diariaradioButtondinheiro:
                        diaria.setFpag("1");
                        break;
                    case R.id.diariaradioButtoncartao:
                        diaria.setFpag("2");
                        break;
                    case R.id.diariaradioButtonvale:
                        diaria.setFpag("3");
                        break;
                }
            }
        });

        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(entrada.getText().toString().equals("Selecionar Data") | saida.getText().toString().equals("Selecionar Data") | valor.getText().toString().equals("")){
                    Toast.makeText(DiariaActivity.this,"Dados incompletos!",Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder confirm = new AlertDialog.Builder(DiariaActivity.this);
                    confirm.setTitle("Confirmar Diária?");
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
                            diaria.setKey_cliente(lista_clientes.get(clientes.getSelectedItemPosition()).getKey());
                            diaria.setKey_quarto(lista_quartos.get(quartos.getSelectedItemPosition()).getKey());
                            diaria.setValor(valor.getText().toString());
                            diaria.setFuncionario(preferencias.getCHAVE_KEY_FUNCIONARIO_NOME());
                            diaria.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("DIARIAS").push().getKey());
                            Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("DIARIAS").child(diaria.getKey()).setValue(diaria);
                            if(diaria.getFpag().equals("3")){
                                Vale vale = new Vale();
                                vale.setData(diaria.getDataloc());
                                vale.setOrigem("Pousada");
                                vale.setStatus("Aberto");
                                vale.setValor_acrecimo("0.00");
                                vale.setValor_desconto("0.00");
                                vale.setValor_pago("0.00");
                                vale.setAtual(diaria.getValor());
                                vale.setValor_total(diaria.getValor());
                                vale.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").push().getKey());
                                vale.setKey_cliente(diaria.getKey_cliente());
                                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES").child(vale.getKey()).setValue(vale);
                                ValeCliente valeCliente = new ValeCliente();
                                valeCliente.setKey(vale.getKey());
                                valeCliente.setSituacao("1");
                                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("VALES_CLIENTES").child(vale.getKey_cliente()).child(vale.getKey()).setValue(valeCliente);
                            }
                            if (diaria.getFpag().equals("1")){
                                ContaCorrente contaCorrente = new ContaCorrente();
                                contaCorrente.setDatacad(diaria.getDataloc());
                                contaCorrente.setKey(Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CONTACORRENTE").push().getKey());
                                contaCorrente.setOrigem("Pousada");
                                contaCorrente.setValor(diaria.getValor());
                                Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CONTACORRENTE").child(contaCorrente.getKey()).setValue(contaCorrente);
                            }
                            Toast.makeText(DiariaActivity.this,"Registrado com sucesso!",Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

                    AlertDialog alertDialog = confirm.create();
                    alertDialog.show();
                }

            }
        });

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
                dataPickerEntrada = new DatePickerDialog(DiariaActivity.this, dateEntrada, myCalendarEntrada
                        .get(Calendar.YEAR), myCalendarEntrada.get(Calendar.MONTH),
                        myCalendarEntrada.get(Calendar.DAY_OF_MONTH));
                dataPickerEntrada.show();
            }
        });

        saida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPickerSaida = new DatePickerDialog(DiariaActivity.this, dateSaida, myCalendarSaida
                        .get(Calendar.YEAR), myCalendarSaida.get(Calendar.MONTH),
                        myCalendarSaida.get(Calendar.DAY_OF_MONTH));
                dataPickerSaida.show();
            }
        });


        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("QUARTOS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Quarto quarto = dados.getValue(Quarto.class);
                    lista_quartos.add(quarto);
                }
                if(lista_quartos.size() > 0){
                    ArrayAdapter<Quarto> adapter = new ArrayAdapter<Quarto>(DiariaActivity.this,
                            android.R.layout.simple_list_item_1,lista_quartos);
                    quartos.setAdapter(adapter);
                }else{
                    Toast.makeText(DiariaActivity.this,"Nenhum quarto cadastrado!",Toast.LENGTH_LONG).show();
                    botaosalvar.setActivated(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Firebase.getDatabaseReference().child(preferencias.getCHAVE_KEY_POSTO_ATIVO()).child("CLIENTE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    ClienteLista quarto = dados.getValue(ClienteLista.class);
                    lista_clientes.add(quarto);
                }
                if(lista_clientes.size() > 0){
                    ArrayAdapter<ClienteLista> adapter = new ArrayAdapter<ClienteLista>(DiariaActivity.this,
                            android.R.layout.simple_list_item_1,lista_clientes);
                    clientes.setAdapter(adapter);
                }else{
                    Toast.makeText(DiariaActivity.this,"Nenhum cliente cadastrado!",Toast.LENGTH_LONG).show();
                    botaosalvar.setActivated(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateDataEntrada() throws ParseException {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        String dataEscolhida = sdf.format(myCalendarEntrada.getTime()); //transforma data em string
        entrada.setText(dataEscolhida);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date d = format.parse(dataEscolhida); //transforma data em tempo sem hora
        diaria.setDataloc(String.valueOf(d.getTime()));
    }

    private void updateDataSaida() throws ParseException {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        String dataEscolhida = sdf.format(myCalendarSaida.getTime()); //transforma data em string
        saida.setText(dataEscolhida);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date d = format.parse(dataEscolhida); //transforma data em tempo sem hora
        diaria.setDatasaida(String.valueOf(d.getTime()));
    }
}
