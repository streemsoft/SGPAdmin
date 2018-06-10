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
import app.streem.sgpadmin.Model.Vale;
import app.streem.sgpadmin.R;

public class ValesAdapter extends RecyclerView.Adapter<ValesAdapter.ViewHolder>{

    private List<Vale> listavales;
    private Context context;
    private Preferencias preferencias;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Vale item);
    }

    public ValesAdapter(List<Vale> listaBombas, Context context, OnItemClickListener listener) {
        this.listavales = listaBombas;
        this.context = context;
        this.preferencias = new Preferencias(context);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_vale, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        Vale bomba = listavales.get(position);
        holder.pago.setText(bomba.getValor_pago());
        holder.desconto.setText(bomba.getValor_desconto());
        holder.acrescimo.setText(bomba.getValor_acrecimo());
        holder.total.setText(bomba.getValor_total());
        holder.atual.setText(bomba.getAtual());
        holder.origem.setText(bomba.getOrigem());
        holder.datal.setText(sdf.format(new Date(Long.parseLong(bomba.getData()))));

        holder.bind(listavales.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listavales.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView origem;
        public TextView atual;
        public TextView total;
        public TextView acrescimo;
        public TextView desconto;
        public TextView pago;
        public TextView datal;
       // public ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            origem = itemView.findViewById(R.id.recyvaleorigem);
            atual = itemView.findViewById(R.id.recyvaleatual);
            total = itemView.findViewById(R.id.recyvaletotal);
            acrescimo = itemView.findViewById(R.id.recyvaleacrescimo);
            desconto = itemView.findViewById(R.id.recyvaledesconto);
            pago = itemView.findViewById(R.id.recyvalepago);
            datal = itemView.findViewById(R.id.recyvaledata);
         //   constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cardViewBombaID);
        }

        public void bind(final Vale item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    listener.onItemClick(item);

                }

            });
        }
    }

}
