package app.streem.sgpadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Lancamento;
import app.streem.sgpadmin.R;

public class LancamentoAdapter extends RecyclerView.Adapter<LancamentoAdapter.ViewHolder>{

    private List<Lancamento> listavales;
    private Context context;
    private Preferencias preferencias;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Lancamento item);
    }

    public LancamentoAdapter(List<Lancamento> listaBombas, Context context, OnItemClickListener listener) {
        this.listavales = listaBombas;
        this.context = context;
        this.preferencias = new Preferencias(context);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_lancamento, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Lancamento bomba = listavales.get(position);
        switch (bomba.getTipo()){
            case 1:
                holder.origem.setText("Abastecimento");
                break;
            case 2:
                holder.origem.setText("Produto");
                break;
            case 3:
                holder.origem.setText("Despesa");
                break;
        }

        switch (bomba.getFpag()){
            case 1:
                holder.fpag.setText("Dinheiro");
                break;
            case 2:
                holder.fpag.setText("Cartão");
                break;
            case 3:
                holder.fpag.setText("Vale");
                break;
        }

        holder.valor.setText(bomba.getValor());
        holder.cliente.setText(bomba.getFuncionario());
        holder.bind(listavales.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listavales.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView origem;
        public TextView fpag;
        public TextView valor;
        public TextView cliente;
        // public ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            origem = itemView.findViewById(R.id.recyclervieworigem);
            fpag = itemView.findViewById(R.id.recyckerviewformapag);
            valor = itemView.findViewById(R.id.recyclerviewvalor);
            cliente = itemView.findViewById(R.id.recyclerviewcliente);
        }

        public void bind(final Lancamento item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    listener.onItemClick(item);

                }

            });
        }
    }

}
