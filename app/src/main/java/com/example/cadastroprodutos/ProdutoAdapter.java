package com.example.cadastroprodutos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends ArrayAdapter<Produto> {

    private final Context context;
    private final List<Produto> listaProdutos;
    private final List<Produto> itensSelecionados = new ArrayList<>();
    private final OnDeletarClickListener listenerDeletar;

    public interface OnDeletarClickListener {
        void onDeletar(Produto produto);
    }

    public ProdutoAdapter(Context context, List<Produto> produtos, OnDeletarClickListener listenerDeletar) {
        super(context, 0, produtos);
        this.context = context;
        this.listaProdutos = produtos;
        this.listenerDeletar = listenerDeletar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_produto, parent, false);
        }

        Produto produtoAtual = listaProdutos.get(position);

        TextView txtInfo = listItem.findViewById(R.id.txtProdutoInfo);
        CheckBox chkItem = listItem.findViewById(R.id.chkItem);
        ImageButton btnDeletar = listItem.findViewById(R.id.btnDeletarItem);

        txtInfo.setText(produtoAtual.toString());

        // Remove listener temporariamente para evitar bugs ao reciclar a view
        chkItem.setOnCheckedChangeListener(null);
        chkItem.setChecked(itensSelecionados.contains(produtoAtual));

        chkItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!itensSelecionados.contains(produtoAtual)) {
                    itensSelecionados.add(produtoAtual);
                }
            } else {
                itensSelecionados.remove(produtoAtual);
            }
        });

        btnDeletar.setOnClickListener(v -> {
            if (listenerDeletar != null) {
                listenerDeletar.onDeletar(produtoAtual);
            }
        });

        return listItem;
    }

    public List<Produto> getItensSelecionados() {
        return itensSelecionados;
    }

    public void selecionarTodos(boolean selecionar) {
        itensSelecionados.clear();
        if (selecionar) {
            itensSelecionados.addAll(listaProdutos);
        }
        notifyDataSetChanged();
    }
}