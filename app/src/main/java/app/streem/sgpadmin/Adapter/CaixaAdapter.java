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
import app.streem.sgpadmin.Model.FechamentoCaixa;
import app.streem.sgpadmin.R;

public class CaixaAdapter extends RecyclerView.Adapter<CaixaAdapter.ViewHolder> {

    private List<FechamentoCaixa> lista;
    private Context context;
    private Preferencias preferencias;

    public CaixaAdapter(List<FechamentoCaixa> lista, Context context) {
        this.lista = lista;
        this.context = context;
        this.preferencias = new Preferencias(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_caixas, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        final FechamentoCaixa lancamento = lista.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        holder.litros.setText(lancamento.getLitros());
        String dataEscolhida = sdf.format(new Date(Long.valueOf(lancamento.getDatacad()))); //transforma data em string
        holder.datacad.setText(dataEscolhida);
        holder.funcionario.setText(lancamento.getFuncionario());
        holder.produtos.setText(String.valueOf(lancamento.getProdutos()));
        holder.despesas.setText(lancamento.getDespesas());
        holder.dinheiro.setText(lancamento.getDinheiro());
        holder.cartao.setText(lancamento.getCartao());
        holder.vale.setText(lancamento.getVale());
        holder.total.setText(lancamento.getTotal());


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView litros;
        public TextView total;
        public TextView cartao;
        public TextView dinheiro;
        public TextView vale;
        public TextView despesas;
        public TextView produtos;
        public TextView datacad;
        public TextView funcionario;


        public ViewHolder(View itemView) {
            super(itemView);

            litros = itemView.findViewById(R.id.recycaixalitros);
            total = itemView.findViewById(R.id.recycaixatotal);
            cartao = itemView.findViewById(R.id.recycaixacartao);
            dinheiro = itemView.findViewById(R.id.recycaixadinheiro);
            vale = itemView.findViewById(R.id.recycaixavale);
            despesas = itemView.findViewById(R.id.recycaixadespesas);
            produtos = itemView.findViewById(R.id.recycaixaprodutos);
            datacad = itemView.findViewById(R.id.recycaixadata);
            funcionario = itemView.findViewById(R.id.recycaixafuncionario);
        }
    }

}
