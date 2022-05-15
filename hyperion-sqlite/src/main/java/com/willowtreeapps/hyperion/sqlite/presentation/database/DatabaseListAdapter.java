package com.willowtreeapps.hyperion.sqlite.presentation.database;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sqlite.R;
import com.willowtreeapps.hyperion.sqlite.helper.database.DatabaseHelper;
import com.willowtreeapps.hyperion.sqlite.helper.database.TableInfo;
import com.willowtreeapps.hyperion.sqlite.model.DatabaseInfo;
import com.willowtreeapps.hyperion.sqlite.presentation.tables.TableItem;

import java.util.List;
import java.util.Map;

class DatabaseListAdapter extends RecyclerView.Adapter<DatabaseListAdapter.DatabaseItemViewHolder> {

    private final List<DatabaseInfo> dbInfoList;
    private OnDatabaseSelectedListener listener;
    private DatabaseListActivity activity;

    public DatabaseListAdapter(List<DatabaseInfo> databaseNames, DatabaseListActivity activity) {
        this.dbInfoList = databaseNames;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DatabaseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hsql_database_file_viewholder, parent, false);
        return new DatabaseItemViewHolder(itemView, listener, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull DatabaseItemViewHolder holder, int position) {
        holder.bind(dbInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return dbInfoList.size();
    }

    public void setListener(OnDatabaseSelectedListener listener) {
        this.listener = listener;
    }

    static class DatabaseItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private static final String TAG = "DatabaseItemViewHolder";

        private DatabaseListActivity activity;

        private final TextView name;

        private DatabaseInfo dbInfo;
        private OnDatabaseSelectedListener l;

        private ImageButton btnMaisConfigs;

        DatabaseItemViewHolder(View itemView, OnDatabaseSelectedListener listener,
               DatabaseListActivity activity) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.hsql_db_name);
            btnMaisConfigs = itemView.findViewById(R.id.hsql_btn_mais_configs);
            this.l = listener;
            this.activity = activity;
        }

        void bind(final DatabaseInfo dbInfo) {
            this.dbInfo = dbInfo;
            name.setText(dbInfo.getDbName());

            btnMaisConfigs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(activity, v);
                    popupMenu.getMenuInflater().inflate(R.menu.hsql_menu_db, popupMenu.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            // BTN APAGAR SELECIONADO
                            if (item.getItemId() == R.id.hsql_menu_db_apagar){
                                popupMenu.dismiss();
                                activity.deleteDatabase(dbInfo.getDbName());
                                activity.carregarBancosDados();
                                return true;
                            }

                            // BTN DETALHES CLICADO
                            if (item.getItemId() == R.id.hsql_menu_db_info){
                                int qtdeTabelas = 0;
                                // REMOVE DO TOTAL DE TABELAS A TABELA DE SEQUENCIA DAS PRIMARY KEYS AUTOINCREMENT
                                if (dbInfo.getTabelas().size() > 1){
                                    qtdeTabelas = dbInfo.getTabelas().size() - 1;
                                }

                                StringBuilder mensagem = new StringBuilder();
                                mensagem.append("Tamanho em bytes: ").append(dbInfo.getDbSizeBytes()).append("\n");
                                mensagem.append("Tamanho em kbytes: ").append(dbInfo.getDbSizeKBytes()).append("\n\n");
                                mensagem.append(qtdeTabelas).append(" TABELAS").append("\n");

                                for (TableItem cursor : dbInfo.getTabelas()) {

                                    // IGNORA A TABELA DE SEQUENCIA DAS PRIMARY KEYS AUTOINCREMENT
                                    if (!cursor.getTableName().equals("sqlite_sequence")) {
                                        long qtdeAproximadaLinhas = cursor.getMaxRowId() - cursor.getMinRowId() + 1;

                                        mensagem.append("- ").append(cursor.getTableName()).append(":").append("\n");
                                        mensagem.append("    - ").append("qtde aprox de linhas:__").append(qtdeAproximadaLinhas).append("\n");
                                        mensagem.append("    - ").append("id maximo:.___________").append(cursor.getMaxRowId()).append("\n");
                                        mensagem.append("    - ").append("id minimo:____________").append(cursor.getMinRowId()).append("\n");

                                    }
                                }

                                new AlertDialog.Builder(activity)
                                    .setTitle("INFORMAÇÕES DO BANCO")
                                    .setMessage(mensagem.toString())
                                    .show();
                                return true;
                            }

                            // BTN SQL BANCO CLICADO
                            if (item.getItemId() == R.id.hsql_menu_db_sql) {
                                StringBuilder mensagem = new StringBuilder();

                                for (Map.Entry<String, TableInfo> cursor : dbInfo.getInfoTabelas().entrySet()) {
                                    // IGNORA A TABELA DE SEQUENCIA DAS PRIMARY KEYS AUTOINCREMENT
                                    if (!cursor.getKey().equals("sqlite_sequence")) {
                                        String prettySql = cursor.getValue().getTableSql();

                                        try {
                                            prettySql = DatabaseHelper.prettySql(prettySql);
                                        } catch (Exception e) {
                                            e.getStackTrace();
                                        }

                                        mensagem.append(prettySql);
                                        mensagem.append("\n\n\n");
                                    }
                                }

                                // ALTERA AS CORES DO VETOR DE STRINGS PARA LARANJA
                                String[] stringsLaranjas = new String[]{"CREATE", "TABLE", "UNIQUE", "CHECK", "PRIMARY KEY", "AUTOINCREMENT"};
                                int corLaranja = Color.parseColor("#f4ad42");

                                // ALTERA AS CORES DO VETOR DE STRINGS PARA AZUL ESCURO
                                String[] stringsAzuis = new String[] {"INTEGER", "TEXT", "NUMERIC", "REAL", "BLOB"};
                                int corAzul = Color.parseColor("#4165f4");

                                // SPANN
                                SpannableString spannableSql = new SpannableString(mensagem.toString());
                                // ALTERA TAMANHO DO TEXTO
                                spannableSql.setSpan(new android.text.style.RelativeSizeSpan(0.7f), 0, spannableSql.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                // ALTERA CORES DAS PALAVRAS LARANJA
                                for (String cursor : stringsLaranjas){
                                    List<Integer[]> allIndexOfWord = DatabaseHelper.allIndexesOf(spannableSql.toString(), cursor);

                                    for (Integer[] intCursor : allIndexOfWord) {
                                        spannableSql.setSpan(new ForegroundColorSpan(corLaranja), intCursor[0], intCursor[1], Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                        spannableSql.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), intCursor[0], intCursor[1], Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                    }
                                }

                                // ALTERA CORES DAS PALAVRAS AZUIS
                                for (String cursor : stringsAzuis) {

                                    List<Integer[]> allIndexeOfWord = DatabaseHelper.allIndexesOf(spannableSql.toString(), cursor);

                                    for (Integer[] intCursor : allIndexeOfWord){
                                        spannableSql.setSpan(new ForegroundColorSpan(corAzul), intCursor[0], intCursor[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        spannableSql.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), intCursor[0], intCursor[1], Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                                    }
                                }

                                new AlertDialog.Builder(activity)
                                    .setTitle("SQL BANCO")
                                    .setMessage(spannableSql)
                                    .show();
                                return true;
                            }

                            return false;
                        }
                    });

                    popupMenu.show(); //showing popup menu
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (l != null) {
                l.onClick(dbInfo.getDbName());
            }
        }
    }

    public interface OnDatabaseSelectedListener {
        void onClick(String databaseName);
    }
}
