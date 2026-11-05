package com.example.cadastroprodutos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editNome, editPreco;
    private TextInputLayout inputLayoutNome, inputLayoutPreco;
    private Button btnSalvar, btnDeletarSelecionados, btnDeletarTudo;
    private CheckBox chkSelecionarTodos;
    private ListView listViewProdutos;

    private ProdutoDbHelper dbHelper;
    private ProdutoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new ProdutoDbHelper(this);

        editNome = findViewById(R.id.editNome);
        editPreco = findViewById(R.id.editPreco);
        inputLayoutNome = findViewById(R.id.inputLayoutNome);
        inputLayoutPreco = findViewById(R.id.inputLayoutPreco);
        btnSalvar = findViewById(R.id.btnSalvar);

        chkSelecionarTodos = findViewById(R.id.chkSelecionarTodos);
        btnDeletarSelecionados = findViewById(R.id.btnDeletarSelecionados);
        btnDeletarTudo = findViewById(R.id.btnDeletarTudo);
        listViewProdutos = findViewById(R.id.listViewProdutos);

        btnSalvar.setOnClickListener(v -> salvarProduto());

        // Marcar/Desmarcar todos os itens
        chkSelecionarTodos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (adapter != null) {
                adapter.selecionarTodos(isChecked);
            }
        });

        // Botão para excluir apenas os selecionados
        btnDeletarSelecionados.setOnClickListener(v -> deletarSelecionados());

        // Botão para excluir todo o banco de dados
        btnDeletarTudo.setOnClickListener(v -> confirmarExclusaoGeral());

        atualizarLista();
    }

    private void salvarProduto() {
        inputLayoutNome.setError(null);
        inputLayoutPreco.setError(null);

        String nome = editNome.getText() != null ? editNome.getText().toString().trim() : "";
        String precoStr = editPreco.getText() != null ? editPreco.getText().toString().trim() : "";

        if (nome.length() < 3) {
            String msgErro = "Nome inválido. Mínimo de 3 caracteres.";
            inputLayoutNome.setError(msgErro);
            Toast.makeText(this, msgErro, Toast.LENGTH_SHORT).show();
            return;
        }

        double preco;
        try {
            preco = Double.parseDouble(precoStr.replace(",", "."));
            if (preco <= 0) {
                String msgErro = "Preço inválido. Informe um valor maior que zero.";
                inputLayoutPreco.setError(msgErro);
                Toast.makeText(this, msgErro, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            String msgErro = "Preço inválido. Informe um valor maior que zero.";
            inputLayoutPreco.setError(msgErro);
            Toast.makeText(this, msgErro, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean sucesso = dbHelper.inserirProduto(nome, preco);

        if (sucesso) {
            Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
            editNome.setText("");
            editPreco.setText("");
            editNome.clearFocus();
            editPreco.clearFocus();
            atualizarLista();
        } else {
            Toast.makeText(this, "Erro ao salvar produto.", Toast.LENGTH_SHORT).show();
        }
    }

    // Confirmação para a Lixeira Vermelha Individual
    private void confirmarExclusaoIndividual(Produto produto) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir o produto '" + produto.getNome() + "'?")
                .setPositiveButton("Sim, Excluir", (dialog, which) -> {
                    if (dbHelper.deletarProduto(produto.getId())) {
                        Toast.makeText(this, "Produto excluído!", Toast.LENGTH_SHORT).show();
                        atualizarLista();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Confirmação para os itens marcados nas Checkboxes
    private void deletarSelecionados() {
        if (adapter == null) return;

        List<Produto> selecionados = adapter.getItensSelecionados();
        if (selecionados.isEmpty()) {
            Toast.makeText(this, "Nenhum item selecionado.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão Múltipla")
                .setMessage("Tem certeza que deseja excluir os " + selecionados.size() + " itens selecionados?")
                .setPositiveButton("Sim, Excluir", (dialog, which) -> {
                    for (Produto p : selecionados) {
                        dbHelper.deletarProduto(p.getId());
                    }
                    Toast.makeText(this, selecionados.size() + " item(ns) removido(s)!", Toast.LENGTH_SHORT).show();
                    atualizarLista();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Confirmação de Segurança para Limpar Todo o Banco
    private void confirmarExclusaoGeral() {
        new AlertDialog.Builder(this)
                .setTitle("ATENÇÃO: Limpar Banco de Dados")
                .setMessage("Tem certeza absoluta que deseja DELETAR TODOS os itens cadastrados? Esta ação não pode ser desfeita.")
                .setPositiveButton("Sim, Limpar Tudo", (dialog, which) -> {
                    dbHelper.limparTudo();
                    Toast.makeText(this, "Todos os registros foram excluídos!", Toast.LENGTH_SHORT).show();
                    atualizarLista();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void atualizarLista() {
        List<Produto> listaProdutos = dbHelper.listarProdutosObjetos();
        adapter = new ProdutoAdapter(this, listaProdutos, this::confirmarExclusaoIndividual);
        listViewProdutos.setAdapter(adapter);
        chkSelecionarTodos.setChecked(false);
    }
}