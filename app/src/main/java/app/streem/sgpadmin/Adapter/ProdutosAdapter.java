package app.streem.sgpadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.streem.sgpadmin.CadProdutoActivity;
import app.streem.sgpadmin.DAO.Preferencias;
import app.streem.sgpadmin.Model.Produto;
import app.streem.sgpadmin.R;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ViewHolder>{

    private List<Produto> listaPrecos;
    private Context context;
    private Preferencias preferencias;

    public ProdutosAdapter(List<Produto> lista, Context context) {
        this.listaPrecos = lista;
        this.context = context;
        this.preferencias = new Preferencias(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_produtos, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Produto lancamento = listaPrecos.get(position);

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));


        holder.valor.setText(lancamento.getPreco());
        if(lancamento.getUlt_venda() == null){
            holder.venda.setText("");
        }else{
            holder.venda.setText(sdf.format(new Date(Long.parseLong(lancamento.getUlt_venda()))));
        }
        holder.compra.setText(sdf.format(new Date(Long.parseLong(lancamento.getUlt_compra()))));

        holder.nomeproduto.setText(lancamento.getNome());
        holder.qtd.setText(lancamento.getQtd());

        holder.botaoeditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CadProdutoActivity.class);
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

        public TextView nomeproduto;
        public TextView compra;
        public TextView venda;
        public TextView valor;
        public TextView qtd;
        public ImageView botaoeditar;

        public ViewHolder(View itemView) {
            super(itemView);

            nomeproduto = itemView.findViewById(R.id.cardprodutonome);
            compra = itemView.findViewById(R.id.cardprecocompra);
            venda = itemView.findViewById(R.id.cardprodutovenda);
            valor = itemView.findViewById(R.id.cardprodutopreco);
            qtd = itemView.findViewById(R.id.cardprodutoquantidade);
            botaoeditar = itemView.findViewById(R.id.cardprecobotaoeditar);
        }
    }
}
