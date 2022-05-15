package com.willowtreeapps.hyperion.sqlite.helper.database;

import androidx.annotation.NonNull;

public class IndiceInfo {

    private long infoIndiceSeqNo;   // A CLASSIFICAÇÃO DA COLUNA NO INDICE (0 SIGNIFICA MAIS À ESQUERDA)
    private int infoIndiceCid;      // A CLASSFICIAÇÃO DA COLUNA NA TABELA QUE ESTÁ SENDO INDEXADA.
    private String infoIndiceName;  // O NOME DA COLUNA QUE ESTÁ SENDO INDEXADA

    // CONSTRUTORES
    public IndiceInfo() {
    }

    // GETTERS AND SETTERS
    public long getInfoIndiceSeqNo() {
        return infoIndiceSeqNo;
    }

    public void setInfoIndiceSeqNo(long infoIndiceSeqNo) {
        this.infoIndiceSeqNo = infoIndiceSeqNo;
    }

    public int getInfoIndiceCid() {
        return infoIndiceCid;
    }

    public void setInfoIndiceCid(int infoIndiceCid) {
        this.infoIndiceCid = infoIndiceCid;
    }

    public String getInfoIndiceName() {
        return infoIndiceName;
    }

    public void setInfoIndiceName(String infoIndiceName) {
        this.infoIndiceName = infoIndiceName;
    }
}
