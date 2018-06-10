package app.streem.sgpadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Bomba;
import app.streem.sgpadmin.R;

public class BombaAdapter extends RecyclerView.Adapter<BombaAdapter.ViewHolder> {

    private List<Bomba> listavales;
    private Context context;
    private Preferencias preferencias;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Bomba item);
    }

    public BombaAdapter(List<Bomba> listaBombas, Context context, OnItemClickListener listener) {
        this.listavales = listaBombas;
        this.context = context;
        this.preferencias = new Preferencias(context);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_bombas, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Bomba bomba = listavales.get(position);
        holder.nome.setText(bomba.getNome());
        holder.contagem.setText(bomba.getCont_final());
        if (bomba.getStatus().equals("1")){
            holder.status.setText("Aberta");
        }else{
            holder.status.setText("Fechada");
        }

        holder.bind(listavales.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listavales.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nome;
        public TextView contagem;
        public TextView status;

        // public ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.recybombanome);
            contagem = itemView.findViewById(R.id.recybombacontagem);
            status = itemView.findViewById(R.id.recybombastatus);

        }

        public void bind(final Bomba item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    listener.onItemClick(item);

                }

            });
        }
    }

}
