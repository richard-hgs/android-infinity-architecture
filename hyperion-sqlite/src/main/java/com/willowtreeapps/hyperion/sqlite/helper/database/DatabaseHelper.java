package com.willowtreeapps.hyperion.sqlite.helper.database;

import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

    /*
     * SQL UTEIS (INICIO)
     */
    // SQL LISTA AS TABELAS DO BANCO DE DADOS
    private static final String SQL_LISTA_TABELAS = "SELECT name FROM sqlite_master WHERE type='table'";

    // SQL LISTA OS INDICES DA TABELA
    private static final String SQL_LISTA_INDICES_TABELA = "pragma index_list('?')"; // ? = Nome da tabela

    // SQL LISTA AS IFORMACOES DO INDICE
    private static final String SQL_LISTA_INFO_INDICE = "pragma index_info('?')"; // ? = Nome do indice

    // SQL LISTA TODAS AS CONSTRAINTS DO BANCO
    private static final String SQL_LISTA_INDICES_BANCO = "SELECT * FROM sqlite_master WHERE type = 'index'";

    // SQL LISTA INFORMACAO DA TABELA
    private static final String SQL_LISTA_INFO_TABELA = "pragma table_info('?')"; // ? = Nome da tabela

    // SQL LISTA AUTOINCREMENT DA TABELA
    private static final String SQL_PRIMARY_KEY_IS_AUTO_INCREMENT = "SELECT COUNT(*) FROM sqlite_sequence WHERE name = '?'"; // ? = Nome da tabela

    // SQL RETORNA SQL DE CRIACAO DO SQLITE_MASTER
    private static final String SQL_CREATION_TABLE = "select sql from sqlite_master where type='table' and name='?'";
    /*
     * SQL UTEIS (FIM)
     */


    /*
     * CONFLICT_CLAUSES(INICIO)
     */
    public static final String CONFLICT_REPLACE = " OR REPLACE ";   // EXECUTA SQL SE CONSTRAINT ANTERIOR EXISTIR APAGA ELA
    public static final String CONFLICT_IGNORE = " OR IGNORE ";     // EXECUTA SQL SE NAO EXISTIR UM COM CONSTRAINT IGUAL
    public static final String CONFLICT_FAIL = " OR FAIL ";         // EXECUTA SQL SE DER ERRO EXIBE ERRO
    public static final String CONFLICT_ABORT = " OR ABORT ";       // EXECUTA SQL SE DER ERRO REVERTE ALTERACOES
    public static final String CONFLICT_ROLL_BACK = " OR ROLLBACK ";//
    public static final String CONFLICT_NONE = " ";
    /*
     * CONFLICT_CLAUSES(FIM)
     */

    /*
     * SEPARATORS (INICIO)
     */
    public static final int SEPARATOR_PARENTESIS = 1;
    /*
     * SEPARATORS (INICIO)
     */

    // CONSTRUTOR
    public DatabaseHelper() {
    }

    // RETORNA VERDADEIRO OU FALSE SE A TABELA EXISTIR NA CONEXAO INFORMADA
    public static boolean tableNameExistsInDb(String tableName, SQLiteDatabase sqLiteDatabase) {
        // RECEBE AS TABELAS DO BANCO DE DADOS
        CursorHelper cursorHelper = new CursorHelper(
                sqLiteDatabase.rawQuery(DatabaseHelper.SQL_LISTA_TABELAS, null)
        );

        // SE O CURSOR POSSUIR ITENS
        if (cursorHelper.moveToFirst()) {
            do {
                String nomeTabelaCursor = cursorHelper.getStringColumn("name");

                if (tableName.equals(nomeTabelaCursor)){
                    return true;
                }
            } while(cursorHelper.moveToNext());
        }
        // FINALIZA O CURSOR
        cursorHelper.close();

        return false;
    }

    // GERA SQL DE INSERCAO
    public static String[] generateInsertOrUpdateSql(@NonNull String conflictClause, @NonNull String tableName,
                                                     @NonNull HashMap<String, Object> columnNamesValues,
                                                     boolean onDuplicateKeyUpdate,
                                                     HashMap<String, Integer> tableColumnUniques) {
        String[] retorno = new String[2];

        StringBuilder sqlInsercao = new StringBuilder();

        sqlInsercao.append("INSERT ");

        sqlInsercao.append(conflictClause);

        /*if (onDuplicateKeyUpdate) {
            sqlInsercao.append(" OR FAIL ");
        }*/

        sqlInsercao.append("INTO ").append(tableName);

        StringBuilder columnNameList = new StringBuilder();
        StringBuilder columnValueList = new StringBuilder();
        StringBuilder columnValueUpdateList = new StringBuilder();
        for (Map.Entry<String, Object> columnNameValue : columnNamesValues.entrySet()) {
            String columnName = columnNameValue.getKey();
            Object columnValue = columnNameValue.getValue();

            columnNameList.append(columnName).append(",");

            columnValueUpdateList.append(columnName).append(" = ");

            if (columnValue instanceof String) {
                columnValueList.append("'").append(columnValue).append("'").append(",");

                columnValueUpdateList.append("'").append(columnValue).append("'").append(",");
            } else {
                columnValueList.append(columnValue).append(",");

                columnValueUpdateList.append(columnValue).append(",");
            }
        }

        sqlInsercao.append("(");
        sqlInsercao.append(columnNameList.replace(columnNameList.lastIndexOf(","), columnNameList.lastIndexOf(",") + 1, ""));
        sqlInsercao.append(")");

        sqlInsercao.append(" VALUES ");

        sqlInsercao.append("(");
        sqlInsercao.append(columnValueList.replace(columnValueList.lastIndexOf(","), columnValueList.lastIndexOf(",") + 1, ""));
        sqlInsercao.append(")");

        // SQL DE UPDATE
        if (onDuplicateKeyUpdate) {
            StringBuilder sqlUpdate = new StringBuilder();
            sqlUpdate.append("UPDATE ").append(tableName).append(" SET ");
            sqlUpdate.append(columnValueUpdateList.replace(columnValueUpdateList.lastIndexOf(","), columnValueUpdateList.lastIndexOf(",") + 1, ""));

            if (tableColumnUniques.size() > 0) {
                sqlUpdate.append(" WHERE ");

                int countUniqueColumns = 1;
                for (Map.Entry<String, Integer> entry : tableColumnUniques.entrySet()) {
                    String nomeColuna = entry.getKey();
                    Integer uniqueColumn = entry.getValue();
                    Object valorColuna = columnNamesValues.get(nomeColuna);

                    if (uniqueColumn == 1) {
                        if (valorColuna instanceof String) {
                            sqlUpdate.append(nomeColuna).append(" = '").append(valorColuna).append("'");
                        } else {
                            sqlUpdate.append(nomeColuna).append(" = ").append(valorColuna).append("");
                        }
                    }

                    // SE A LISTA DE COLUNAS UNICAS POSSUIR MAIS ITENS ADICIONA O AND PARA RECEBELAS
                    if (countUniqueColumns < tableColumnUniques.size()) {
                        sqlUpdate.append(" AND ");
                    }

                    countUniqueColumns++;
                }
            }

            retorno[1] = sqlUpdate.toString();
        }

        retorno[0] = sqlInsercao.toString();

        return retorno;
    }

    // GERA SQL DE INSERCAO COM DADOS DE OUTRA TABELA
    public static String generateInsertFromAnotherTable(@NonNull String conflictClause,
                                                        @NonNull String tableNameInsert,
                                                        @NonNull HashMap<String, Object> columnNameValuesInsert,
                                                        @NonNull String tableNameSelect,
                                                        @NonNull HashMap<String, Object> columnNameValuesSelect,
                                                        @Nullable String order) {
        StringBuilder sqlInsercao = new StringBuilder();

        sqlInsercao.append("INSERT ");

        sqlInsercao.append(conflictClause);

        /*if (onDuplicateKeyUpdate) {
            sqlInsercao.append(" OR FAIL ");
        }*/

        sqlInsercao.append("INTO ").append(tableNameInsert);

        StringBuilder columnNameInsertList = new StringBuilder();
        for (Map.Entry<String, Object> columnNameInsertValue : columnNameValuesInsert.entrySet()) {
            String columnName = columnNameInsertValue.getKey();
            //Object columnValue = columnNameValue.getValue();

            columnNameInsertList.append(columnName).append(",");
        }

        StringBuilder columnNameSelectList = new StringBuilder();
        for (Map.Entry<String, Object> columnNameSelectValue : columnNameValuesSelect.entrySet()) {
            String columnName = columnNameSelectValue.getKey();
            //Object columnValue = columnNameValue.getValue();

            columnNameSelectList.append(columnName).append(",");
        }

        sqlInsercao.append("(");
        sqlInsercao.append(
            columnNameInsertList.replace( // REMOVE ULTIMA VIRGULA DA SQL
                columnNameInsertList.lastIndexOf(","),
                columnNameInsertList.lastIndexOf(",") + 1,
                ""
            )
        );
        sqlInsercao.append(") ");
        sqlInsercao.append(" SELECT ");
        sqlInsercao.append(
                columnNameSelectList.replace( // REMOVE ULTIMA VIRGULA DA SQL
                columnNameSelectList.lastIndexOf(","),
                columnNameSelectList.lastIndexOf(",") + 1,
                ""
            )
        );
        sqlInsercao.append(" FROM ");
        sqlInsercao.append(tableNameSelect);

        if (order != null) {
            sqlInsercao.append(order);
        }

        return sqlInsercao.toString();
    }

    // RETORNA INFORMACOES DA TABELA SELECIONADA
    public static TableInfo getTableInfo(String tableName, @NonNull SQLiteDatabase sqLiteDatabase) throws Exception {
        TableInfo tableInfo = new TableInfo();

        HashMap<String, TableColumnInfo> tableColumnsInfo = new HashMap<>();

        // RECEBE INFORMACOES DA COLUNA DA TABELA
        CursorHelper cursorHelper = new CursorHelper(
            sqLiteDatabase.rawQuery(SQL_LISTA_INFO_TABELA.replace("?", tableName), null)
        );
        if (cursorHelper.moveToFirst()) {
            do {
                int colunaId = cursorHelper.getIntColumn("cid");
                String colunaNome = cursorHelper.getStringColumn("name");
                String colunaTipo = cursorHelper.getStringColumn("type");
                int colunaNotNull = cursorHelper.getIntColumn("notnull");
                String colunaValorPadrao = cursorHelper.getStringColumn("dflt_value");
                int colunaPk = cursorHelper.getIntColumn("pk");
                int colunaAi = 0;

                if (colunaPk == 1) {
                    CursorHelper cursorAi = new CursorHelper(
                        sqLiteDatabase.rawQuery(SQL_PRIMARY_KEY_IS_AUTO_INCREMENT.replace("?", tableName), null)
                    );
                    if (cursorAi.moveToFirst()){
                        colunaAi = cursorAi.getInt(0);
                    }
                }

                tableColumnsInfo.put(colunaNome, new TableColumnInfo(
                    colunaId,
                    colunaNome,
                    colunaTipo,
                    colunaValorPadrao,
                    colunaPk,
                    colunaNotNull,
                    colunaAi
                ));
            } while(cursorHelper.moveToNext());
        }
        cursorHelper.close();

        // RECEBE INFORMACOES DAS CONSTRAINTS DAS COLUNAS DA TABELA
        CursorHelper cursorIndices = new CursorHelper(
            sqLiteDatabase.rawQuery(SQL_LISTA_INDICES_TABELA.replace("?", tableName), null)
        );
        if (cursorIndices.moveToFirst()) {
            do {
                String indiceName = cursorIndices.getStringColumn("name");
                long indiceSeq = cursorIndices.getLongColumn("seq");
                int indiceUnique = cursorIndices.getIntColumn("unique");
                String indiceOrigin = cursorIndices.getStringColumn("origin");
                int indicePartial = cursorIndices.getIntColumn("partial");

                CursorHelper cursorInfoIndice = new CursorHelper(
                        sqLiteDatabase.rawQuery(SQL_LISTA_INFO_INDICE.replace("?", indiceName), null)
                );
                if (cursorInfoIndice.moveToFirst()) {
                    long infoIndiceSeqNo = cursorInfoIndice.getLongColumn("seqno");
                    int infoIndiceCid = cursorInfoIndice.getIntColumn("cid");
                    String infoIndiceName = cursorInfoIndice.getStringColumn("name");

                    TableColumnInfo tableColumnInfo = tableColumnsInfo.get(infoIndiceName);
                    if (tableColumnInfo != null) {
                        tableColumnInfo.setColumnUnique(indiceUnique);
                    }

                    //tableColumnsInfo.put(infoIndiceName, tableColumnInfo);
                }
                cursorInfoIndice.close();

            } while(cursorIndices.moveToNext());
        }
        cursorIndices.close();

        // RECEBE CHECK CONSTRAINTS DA TABELA
        String sqlCriacaoTabela = getCreateSqlFromTable(sqLiteDatabase, tableName);

        // RECEBER SQL DE CRIACAO DAS COLUNAS
        String sqlColunasTabela = sqlCriacaoTabela.substring(
          sqlCriacaoTabela.indexOf("(") + 1,
          sqlCriacaoTabela.lastIndexOf(")")
        );

        // RECEBE COLUNA POR COLUNA
        String[] colunasTabela = sqlColunasTabela.split(",");

        // PARA CADA COLUNA
        for (String coluna : colunasTabela) {
            if (coluna.length() > 0 && coluna.contains(" ")) {
                // ADICIONA OS PARAMETROS DESEJADOS NA TABELA
                String nomeColuna = coluna.substring(0, coluna.indexOf(" "));
                String valColuna = coluna.substring(coluna.indexOf(" ") + 1);

                if (coluna.contains(" CHECK")) {
                    String checkColunaStart = valColuna.substring(valColuna.indexOf(" CHECK") + 6);

                    int posOfCheckEndParentesis = -1;
                    char[] valColunaChars = checkColunaStart.toCharArray();

                    int openedParentesis = 0;
                    int closedParentesis = 0;
                    for (int i = 0; i < valColunaChars.length; i++) {
                        // COUNT TOTAL PARENTESIS OPENED
                        if (valColunaChars[i] == '(') {
                            openedParentesis++;
                        }

                        // COUNT TOTAL PARENTESIS CLOSED
                        if (valColunaChars[i] == ')') {
                            closedParentesis++;
                        }

                        // SE QUANTIDADE DE PARENTESIS ABERTO FOR IGUAL A FECHADA O FIM DO CHECK CHEGOU
                        if (openedParentesis > 0 && closedParentesis > 0
                                && openedParentesis == closedParentesis) {
                            posOfCheckEndParentesis = i;
                            break;
                        }
                    }

                    // SALVA O VALOR DO CHECK
                    String checkValue = checkColunaStart.substring(1, posOfCheckEndParentesis);

                    // LogHelper.d("check: " + );

                    // ADICIONA OS DADOS AO OBJETO
                    TableColumnInfo tableColumnInfo = tableColumnsInfo.get(nomeColuna);
                    if (tableColumnInfo != null) {
                        tableColumnInfo.setColumnSql(valColuna);
                        tableColumnInfo.setColumnCheck(checkValue);
                    }
                }
            }
        }

        // ADICIONA SQL DE CRIACAO DA TABELA
        tableInfo.setTableSql(sqlCriacaoTabela);

        // ADICIONA AS COLUNAS DA TABELA
        tableInfo.setTableColumns(tableColumnsInfo);

        //LogHelper.d(tableInfo.toString());

        return tableInfo;
    }

    // RETORNA SQL DE CRIACAO DA TABELA SELECIONADA
    public static String getCreateSqlFromTable(SQLiteDatabase sqLiteDatabase, String nomeTabela)
            throws Exception {
        String retorno = "";

        CursorHelper cursorHelper = new CursorHelper(
            sqLiteDatabase.rawQuery(SQL_CREATION_TABLE.replace("?", nomeTabela), null)
        );
        if (cursorHelper.moveToFirst()) {
            retorno = cursorHelper.getStringColumn("sql");
        }

        return retorno;
    }

    // ORGANIZA SQL
    public static String prettySql(String sql) throws Exception {
        String identStr1 = " ";
        String identStr2 = "  ";

        StringBuilder retornoPretty = new StringBuilder();

        String createBeforeStartParentesis = sql.substring(0, sql.indexOf("("));
        String colunasSql = getStrInsideSeparators(sql.substring(sql.indexOf("(")), SEPARATOR_PARENTESIS);

        String colunaSql[] = colunasSql.split(",");

        retornoPretty.append(createBeforeStartParentesis);
        retornoPretty.append("\n");
        retornoPretty.append(identStr1).append("(").append("\n");
        for (int i=0; i<colunaSql.length; i++) {
            retornoPretty.append(identStr2).append(colunaSql[i]);

            if (i == colunaSql.length - 1) {
                retornoPretty.append("\n");
            } else {
                retornoPretty.append(",").append("\n");
            }
        }
        retornoPretty.append(identStr1).append(")");

        return retornoPretty.toString();
    }

    // RETORNA STRING DENTRO DOS SEPARADORES
    @NonNull
    public static String getStrInsideSeparators(@NonNull String s, int separator) throws Exception {
        int posOfEndSeparator = -1;
        char[] strChars = s.toCharArray();

        char[] strSeparators = getStrSeparatorFromInt(separator);

        if (strSeparators != null) {
            int openedSeparators = 0;
            int closedSeparators = 0;
            for (int i = 0; i < strChars.length; i++) {
                // COUNT TOTAL SEPARATORS OPENED
                if (strChars[i] == strSeparators[0]) {
                    openedSeparators++;
                }

                // COUNT TOTAL SEPARATORS CLOSED
                if (strChars[i] == strSeparators[1]) {
                    closedSeparators++;
                }

                // SE QUANTIDADE DE PARENTESIS ABERTO FOR IGUAL A FECHADA O FIM DO CHECK CHEGOU
                if (openedSeparators > 0 && closedSeparators > 0
                        && openedSeparators == closedSeparators) {
                    posOfEndSeparator = i;
                    break;
                }
            }
        }

        // SALVA O VALOR DO CHECK
        return s.substring(1, posOfEndSeparator);
    }

    // CONVERTE INT SEPARATOR TO STRING SEPARATOR START END
    @Nullable
    private static char[] getStrSeparatorFromInt(int separator){
        switch(separator){
            case(SEPARATOR_PARENTESIS):
                return new char[]{'(', ')'};
            default:
                return null;
        }
    }

    // RETORNA INT[] 0= POS INCIAL 1= POS FINAL DA PALAVRA SELECIONADA PARA A STRING INFORMADA
    private int[] getWordPos(String str, String word) throws Exception {
        return new int[]{str.indexOf(word), str.indexOf(word) + word.length()};
    }

    public static List<Integer[]> allIndexesOf(String str, String value) {
        List<Integer[]> indexes = new ArrayList<>();
        for (int index = 0;; index += value.length()) {
            index = str.indexOf(value, index);
            if (index == -1) {
                return indexes;
            }
            indexes.add(new Integer[]{index, index + value.length()});
        }
    }
}
