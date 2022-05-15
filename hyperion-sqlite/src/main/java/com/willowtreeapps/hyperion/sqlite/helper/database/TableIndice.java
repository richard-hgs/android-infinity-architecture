package com.willowtreeapps.hyperion.sqlite.helper.database;

import androidx.annotation.NonNull;


public class TableIndice {

    private String indiceName;  // O NOME DO INDICE
    private long indiceSeq;     // UM NUMERO SEQUENCIAL ASSOCIADO PARA CADA INDICE PARA PROPÓSITOS DE RASTREAMENTO INTERNOS
    private int indiceUnique;   // 1 SE INDICE FOR "UNIQUE" 0 SE NÃO
    private String indiceOrigin;// "c"  SE O INDICE FOR CRIADO POR: CREATE INDEX statement,
                                // "u"  SE O INDICE FOR CRIADO POR: UNIQUE constraint,
                                // "pk" SE O INDICE FOR CRIADO POR: PRIMARY KEY constraint.
    private int indicePartial;  // 1 SE O INDICE FOR PARCIAL 0 SE NAO

    // CONSTRUTORES
    public TableIndice() {
    }

    // GETTERS AND SETTERS
    public String getIndiceName() {
        return indiceName;
    }

    public void setIndiceName(String indiceName) {
        this.indiceName = indiceName;
    }

    public long getIndiceSeq() {
        return indiceSeq;
    }

    public void setIndiceSeq(long indiceSeq) {
        this.indiceSeq = indiceSeq;
    }

    public int getIndiceUnique() {
        return indiceUnique;
    }

    public void setIndiceUnique(int indiceUnique) {
        this.indiceUnique = indiceUnique;
    }

    public String getIndiceOrigin() {
        return indiceOrigin;
    }

    public void setIndiceOrigin(String indiceOrigin) {
        this.indiceOrigin = indiceOrigin;
    }

    public int getIndicePartial() {
        return indicePartial;
    }

    public void setIndicePartial(int indicePartial) {
        this.indicePartial = indicePartial;
    }
}
