package com.willowtreeapps.hyperion.sqlite.presentation.records;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.willowtreeapps.hyperion.sqlite.R;
import com.willowtreeapps.hyperion.sqlite.domain.usecase.records.RecordsViewerUseCase;
import com.willowtreeapps.hyperion.sqlite.domain.usecase.records.RecordsViewerUseCaseImpl;
import com.willowtreeapps.hyperion.sqlite.presentation.DatabaseViewModel;
import com.willowtreeapps.hyperion.sqlite.ui.CustomHorizontalScrollView;
import com.willowtreeapps.hyperion.sqlite.ui.CustomScrollView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DbRecordsViewModel extends DatabaseViewModel<RecordsViewerUseCase> {

    public static final int FIELD_TYPE_NULL = 0;
    public static final int FIELD_TYPE_INTEGER = 1;
    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_STRING = 3;
    public static final int FIELD_TYPE_BLOB = 4;

    public void createTableRows(final Context context,
                                final String tableName,
                                Consumer<List<TableRow>> subscriber,
                                final CustomHorizontalScrollView customHorizontalScrollView,
                                final DbRecordViewerActivity dbRecordViewerActivity,
                                final CustomScrollView customScrollView) {
        //this.parentView = parentView;


        subscriptions.add(getUseCase().fetchRecords(tableName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Cursor, List<TableRow>>() {
                    @Override
                    public List<TableRow> apply(Cursor cursor) throws Exception {
                        return getTableRows(context, cursor, tableName, dbRecordViewerActivity,
                                customHorizontalScrollView, customScrollView);
                    }
                })
                .subscribe(subscriber));
    }


    //Derived from https://github.com/infinum/android_dbinspector/blob/master/dbinspector/src/main/java/im/dino/dbinspector/adapters/TablePageAdapter.java
    @SuppressLint("ClickableViewAccessibility")
    private List<TableRow> getTableRows(Context context,
                                        final Cursor cursor, final String tableName,
                                        final DbRecordViewerActivity dbRecordViewerActivity,
                                        final CustomHorizontalScrollView customHorizontalScrollView,
                                        final CustomScrollView customScrollView) {

        final List<TableRow> rows = new ArrayList<>();
        TableRow header = new TableRow(context);
        cursor.moveToFirst();

        int paddingPx = context.getResources().getDimensionPixelSize(R.dimen.hsql_column_padding);

        for (int col = 0; col < cursor.getColumnCount() + 1; col++) {
            // SE FOREM AS COLUNAS DA TABELA ENTAO
            if (col < cursor.getColumnCount()) {
                // CRIA O TEXTO PARA EXIBIR O NOME DAS COLUNAS
                final TextView textView = new TextView(context);
                textView.setText(cursor.getColumnName(col));
                textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setMaxLines(1);

                // CRIA O LAYOUT PARA AGRUPAR AS VIEWS
                RelativeLayout relativeLayout = new RelativeLayout(context);
                relativeLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                relativeLayout.addView(textView);

                // CRIA O BOTAO QUE AJUSTA A LARGURA DA COLUNA
                final Button btnStretch = new Button(context);
                relativeLayout.addView(btnStretch);
                RelativeLayout.LayoutParams layBtnStretch = new RelativeLayout.LayoutParams(
                        40, 40
                );
                layBtnStretch.addRule(RelativeLayout.ALIGN_PARENT_END);
                layBtnStretch.addRule(RelativeLayout.ALIGN_END, textView.getId());
                layBtnStretch.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layBtnStretch.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                btnStretch.setLayoutParams(layBtnStretch);
                // CRIA A ACAO DE DESLIZAR O DEDO QUE ALTERA A LARGURA DAS COLUNAS
                final int finalCol = col;
                btnStretch.setOnTouchListener(new View.OnTouchListener() {
                    private int _xDelta;
                    private int _yDelta;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                // _xDelta and _yDelta record how far inside the view we have touched. These
                                // values are used to compute new margins when the view is moved.
                                _xDelta = X - view.getLeft();
                                _yDelta = Y - view.getTop();

                                // DEDO PRESSIONADO BLOQUEIA O SCROLL
                                if (customHorizontalScrollView != null) {
                                    customHorizontalScrollView.setScrollEnable(false);
                                }
                                if (customScrollView != null){
                                    customScrollView.setScrollEnable(false);
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                // DEDO LEVANTOU HABILITA SCROLL
                                if (customHorizontalScrollView != null) {
                                    customHorizontalScrollView.setScrollEnable(true);
                                }
                                if (customScrollView != null){
                                    customScrollView.setScrollEnable(true);
                                }
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                            case MotionEvent.ACTION_POINTER_UP:
                                // Do nothing
                                break;
                            case MotionEvent.ACTION_MOVE:
                                // DEDO SE MOVEU

                                // ORGANIZA LARGURA DO CABEÇALHO
                                RelativeLayout.LayoutParams txtLayParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                                txtLayParams.width = X - _xDelta;
                                textView.setLayoutParams(txtLayParams);

                                // ORGANIZA A LARGURA DAS LINHAS DA COLUNA ATUAL
                                for (int i = 1; i < rows.size(); i++) {
                                    TableRow tempRow = rows.get(i);
                                    RelativeLayout tempRelatLay = (RelativeLayout) tempRow.getChildAt(finalCol);
                                    TextView txtDaColuna = (TextView) tempRelatLay.findViewById(10 + finalCol);
                                    txtDaColuna.setWidth(X - _xDelta);
                                }
                                break;
                        }
                        return true;
                    }
                });

                header.addView(relativeLayout);
            } else { // COLUNA DE CONFIGURACOES
                // CRIA O TEXTO PARA EXIBIR O NOME DAS COLUNAS
                final TextView textView = new TextView(context);
                textView.setText("---- CONFIGS ----");
                textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setMaxLines(1);

                // CRIA O LAYOUT PARA AGRUPAR AS VIEWS
                RelativeLayout relativeLayout = new RelativeLayout(context);
                relativeLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                relativeLayout.addView(textView);

                header.addView(relativeLayout);
            }
        }

        rows.add(header);

        boolean alternate = true;

        if (cursor.getCount() == 0) {
            return rows;
        }

        do {
            final TableRow row = new TableRow(context);
            for (int col = 0; col < cursor.getColumnCount() + 1; col++) {
                // SE FOR AS COLUNAS DA TABELA ENTAO
                if (col < cursor.getColumnCount()) {
                    // CRIA A TEXT VIEW QUE EXIBE OS DADOS DA TABELA
                    TextView textView = new TextView(context);
                    textView.setId(10 + col);
                    textView.setMaxLines(1);
                    if (cursor.getType(col) == FIELD_TYPE_BLOB) {
                        //We don't need to display the contents of the blob
                        textView.setText(R.string.hsql_data_type_blob);
                    } else {
                        textView.setText(cursor.getString(col));
                    }
                    textView.setPadding(paddingPx, paddingPx / 2, paddingPx, paddingPx / 2);
                    int rowBgRes;
                    if (alternate) {
                        rowBgRes = R.color.hsql_table_row_colour_alt;
                    } else {
                        rowBgRes = R.color.hsql_table_row_colour_standard;
                    }

                    // LAYOUT QUE ARMAZENA AS VIEWS DA LINHA
                    RelativeLayout relativeLayout = new RelativeLayout(context);
                    relativeLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    relativeLayout.setBackgroundColor(context.getResources().getColor(rowBgRes));

                    // ADICIONA A TEXT VIEW COM OS DADOS DA LINHA DA TABELA
                    relativeLayout.addView(textView);

                    // CRIA O DIVISOR DIREITO
                    View viewDivisorDireito = new View(context);
                    viewDivisorDireito.setBackgroundColor(Color.parseColor("#AAAAAA"));
                    RelativeLayout.LayoutParams layParamDivDir = new RelativeLayout.LayoutParams(
                            dpToPixel(context, 1),
                            0
                    );
                    layParamDivDir.addRule(RelativeLayout.ALIGN_PARENT_END);
                    layParamDivDir.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layParamDivDir.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layParamDivDir.rightMargin = dpToPixel(context, 6);
                    viewDivisorDireito.setLayoutParams(layParamDivDir);

                    // ADICIONA O DIVISOR DIREITO AO LAYOUT
                    relativeLayout.addView(viewDivisorDireito);

                    row.addView(relativeLayout);
                } else { // COLUNA DE CONFIGURACAO
                    // CRIA A TEXT VIEW QUE EXIBE OS DADOS DA TABELA
                    ImageView imgBtnApagar = new ImageView(context);
                    imgBtnApagar.setImageDrawable(context.getResources().getDrawable(R.drawable.hsql_ic_delete_red_24dp));

                    int rowBgRes;
                    if (alternate) {
                        rowBgRes = R.color.hsql_table_row_colour_alt;
                    } else {
                        rowBgRes = R.color.hsql_table_row_colour_standard;
                    }

                    // LAYOUT QUE ARMAZENA AS VIEWS DA LINHA
                    RelativeLayout relativeLayout = new RelativeLayout(context);
                    relativeLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    relativeLayout.setBackgroundColor(context.getResources().getColor(rowBgRes));

                    // ADICIONA A TEXT VIEW COM OS DADOS DA LINHA DA TABELA
                    relativeLayout.addView(imgBtnApagar);

                    RelativeLayout.LayoutParams layParamsBtnApagar = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        0
                    );
                    layParamsBtnApagar.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    layParamsBtnApagar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    imgBtnApagar.setLayoutParams(layParamsBtnApagar);

                    imgBtnApagar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView txtValorPrimeiraColuna = (TextView) ((RelativeLayout) row.getChildAt(0)).getChildAt(0);

                            try {
                                long id = Long.parseLong(txtValorPrimeiraColuna.getText().toString());
                                String coluna = cursor.getColumnNames()[0];

                                db.execSQL("DELETE FROM " + tableName + " WHERE " + coluna + " = " + id);

                                dbRecordViewerActivity.setUpTable();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    row.addView(relativeLayout);
                }
            }

            alternate = !alternate;
            rows.add(row);

        } while (cursor.moveToNext());

        cursor.close();
        return rows;
    }


    @Override
    protected RecordsViewerUseCase createUsecase(SQLiteDatabase db) {
        return new RecordsViewerUseCaseImpl(db);
    }

    //convert int to dp e retorna em pixels int
    public static int dpToPixel(Context activity, int dp) {
        float density = activity.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
