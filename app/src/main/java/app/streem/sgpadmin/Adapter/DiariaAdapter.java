package app.streem.sgpadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Diaria;
import app.streem.sgpadmin.R;

public class DiariaAdapter extends RecyclerView.Adapter<DiariaAdapter.ViewHolder> {

    private List<Diaria> lista;
    private Context context;
    private Preferencias preferencias;

    public DiariaAdapter(List<Diaria> lista, Context context) {
        this.lista = lista;
        this.context = context;
        this.preferencias = new Preferencias(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_diarias, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        final Diaria lancamento = lista.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        holder.valor.setText(lancamento.getValor());
        String dataEscolhida = sdf.format(new Date(Long.valueOf(lancamento.getDataloc()))); //transforma data em string
        holder.entrada.setText(dataEscolhida);
        dataEscolhida = sdf.format(new Date(Long.valueOf(lancamento.getDatasaida()))); //transforma data em string
        holder.saida.setText(dataEscolhida);
        holder.cliente.setText(lancamento.getKey_cliente());
        holder.quarto.setText(lancamento.getKey_quarto());
        switch (lancamento.getFpag()){
            case "1":
                holder.fpag.setText("Dinheiro");
                break;
            case "2":
                holder.fpag.setText("Cartão");
                break;
            case "3":
                holder.fpag.setText("Vale");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView valor;
        public TextView entrada;
        public TextView saida;
        public TextView cliente;
        public TextView fpag;
        public TextView quarto;

        public ViewHolder(View itemView) {
            super(itemView);

            valor = itemView.findViewById(R.id.recydiariavalor);
            entrada = itemView.findViewById(R.id.recydiariaentrada);
            saida = itemView.findViewById(R.id.recydiariasaida);
            cliente = itemView.findViewById(R.id.recydiariacliente);
            fpag = itemView.findViewById(R.id.recydiariafpag);
            quarto = itemView.findViewById(R.id.recydiariaquarto);
        }
    }
}
