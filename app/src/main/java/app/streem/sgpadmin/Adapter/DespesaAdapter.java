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

import app.streem.sgpadmin.CadProdutoActivity;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Lancamento;
import app.streem.sgpadmin.R;

public class DespesaAdapter extends RecyclerView.Adapter<DespesaAdapter.ViewHolder>{

    private List<Lancamento> lista;
    private Context context;
    private Preferencias preferencias;

    public DespesaAdapter(List<Lancamento> lista, Context context) {
        this.lista = lista;
        this.context = context;
        this.preferencias = new Preferencias(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_despesa, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Lancamento lancamento = lista.get(position);

        holder.valor.setText(lancamento.getValor());
        holder.funcionario.setText(lancamento.getFuncionario());
        holder.desc.setText(lancamento.getLitro_qtd());
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        String dataEscolhida = sdf.format(new Date(Long.valueOf(lancamento.getDatacad()))); //transforma data em string
        holder.datacad.setText(dataEscolhida);

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView valor;
        public TextView funcionario;
        public TextView desc;
        public TextView datacad;

        public ViewHolder(View itemView) {
            super(itemView);

            valor = itemView.findViewById(R.id.recydespesavalor);
            funcionario = itemView.findViewById(R.id.recydespesafuncionario);
            desc = itemView.findViewById(R.id.recydespesaobservacao);
            datacad = itemView.findViewById(R.id.recydespesadata);
        }
    }


}
