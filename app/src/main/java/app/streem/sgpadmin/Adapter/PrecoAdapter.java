package app.streem.sgpadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.streem.sgpadmin.CadPrecosActivity;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Preco;
import app.streem.sgpadmin.R;

public class PrecoAdapter extends RecyclerView.Adapter<PrecoAdapter.ViewHolder>{

    private List<Preco> listaPrecos;
    private Context context;
    private Preferencias preferencias;

    public PrecoAdapter(List<Preco> lista, Context context) {
        this.listaPrecos = lista;
        this.context = context;
        this.preferencias = new Preferencias(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_precos, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Preco lancamento = listaPrecos.get(position);

        holder.valor.setText(lancamento.getValor());
        holder.combustivel.setText(lancamento.getCombustivel());
        holder.data.setText(lancamento.getData());

        holder.botaoeditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CadPrecosActivity.class);
                intent.putExtra("obj",lancamento);
                intent.putExtra("acao","2");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaPrecos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView combustivel;
        public TextView data;
        public TextView valor;
        public ImageView botaoeditar;

        public ViewHolder(View itemView) {
            super(itemView);

            combustivel = itemView.findViewById(R.id.cardprodutonome);
            data = itemView.findViewById(R.id.cardprecocompra);
            valor = itemView.findViewById(R.id.cardprodutovenda);
            botaoeditar = itemView.findViewById(R.id.cardprecobotaoeditar);
        }
    }
}
