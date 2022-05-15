package com.willowtreeapps.hyperion.sqlite.presentation.database;

import androidx.lifecycle.ViewModelProviders;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;
import com.willowtreeapps.hyperion.sqlite.R;
import com.willowtreeapps.hyperion.sqlite.helper.database.DatabaseHelper;
import com.willowtreeapps.hyperion.sqlite.helper.database.TableInfo;
import com.willowtreeapps.hyperion.sqlite.model.DatabaseInfo;
import com.willowtreeapps.hyperion.sqlite.presentation.tables.TableItem;
import com.willowtreeapps.hyperion.sqlite.presentation.tables.TableViewModel;
import com.willowtreeapps.hyperion.sqlite.presentation.tables.TablesListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.functions.Consumer;

@HyperionIgnore
public class DatabaseListActivity extends AppCompatActivity
        implements DatabaseListAdapter.OnDatabaseSelectedListener {

    private static final String TAG = "DatabaseListActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsql_database_list);
        // CORRIGE ERRO EM QUE O TEMA JA POSSUI ACTION BAR
        if (getSupportActionBar() == null){
            setSupportActionBar((Toolbar) findViewById(R.id.hsql_toolbar));
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.hsql_database_list_heading);
        }

        carregarBancosDados();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private List<DatabaseInfo> fetchDatabaseList() {

        Set<DatabaseInfo> dbInfoList = new HashSet<>();
        for (String db : databaseList()) {
            if (!db.endsWith("-journal") && !db.endsWith("-wal") && !db.endsWith("-shm")) {
                // RECEBE TAMANHO DO BANCO DE DADOS
                File f = getDatabasePath(db);
                final long dbSizeBytes = f.length();
                long dbSizeInKbytes = dbSizeBytes / 1024;

                // CONEXAO COM O BANCO DE DADOS
                final SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(f.getPath(),
                        null, SQLiteDatabase.OPEN_READONLY);

                // ARMAZENA OS DADOS DAS TABELAS
                // RECEBE INFORMACOES DAS TABELAS DO BANCO
                final HashMap<String, TableInfo> tablesInfo = new HashMap<>();

                // RECEBE NOME DAS TABELAS DO BANCO DE DADOS
                final TableViewModel tableViewModel = initViewModels(db);
                final List<TableItem> tableList = new ArrayList<>();
                tableViewModel.loadTables(new Consumer<List<TableItem>>() {
                    @Override
                    public void accept(List<TableItem> tableItems) throws Exception {
                        tableList.addAll(tableItems);

                        for (int i=0; i<tableItems.size(); i++) {
                            TableItem tableItem = tableItems.get(i);
                            // IGNORA A TABELA DE SQUENCIA DE AUTO INCREMENTS
                            if (!tableItem.getTableName().equals("sqlite_sequence")) {
                                // RECEBE ID_MINIMO e ID_MAXIMO DAS TABELAS PARA CALCULAR QTDE MEDIA DE LINHAS
                                tableItem.setMaxRowId(tableViewModel.getMaxRowId(tableItems.get(i).getTableName()));
                                tableItem.setMinRowId(tableViewModel.getMinRowId(tableItems.get(i).getTableName()));

                                tablesInfo.put(tableItem.getTableName(), DatabaseHelper.getTableInfo(tableItem.getTableName(), sqLiteDatabase));
                            }

                            if (i == tableItems.size() - 1){
                                // FINALIZA CONEXAO COM O BANCO
                                sqLiteDatabase.close();
                            }
                        }
                    }
                });

                DatabaseInfo dbInfo = new DatabaseInfo();
                dbInfo.setDbName(db);
                dbInfo.setDbSizeBytes(dbSizeBytes);
                dbInfo.setDbSizeKBytes(dbSizeInKbytes);
                dbInfo.setTabelas(tableList);
                dbInfo.setInfoTabelas(tablesInfo);

                dbInfoList.add(dbInfo);
            }
        }
        return new ArrayList<>(dbInfoList);
    }

    public void carregarBancosDados() {
        final RecyclerView list = findViewById(R.id.hsql_list);
        list.setLayoutManager(new LinearLayoutManager(this));

        final List<DatabaseInfo> databaseList = fetchDatabaseList();
        DatabaseListAdapter adapter = new DatabaseListAdapter(databaseList, this);
        adapter.setListener(this);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(String databaseName) {
        TablesListActivity.startActivity(this, databaseName);
    }

    private TableViewModel initViewModels(String databaseName) {
        final File dbFile = getDatabasePath(databaseName);
        TableViewModel viewModel = ViewModelProviders.of(this).get(TableViewModel.class);
        viewModel.initDatabase(dbFile);
        return viewModel;
    }
}
