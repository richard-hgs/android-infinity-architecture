package com.infinity.architecture.base.annotations.utils;

public interface FilterBy {

    // RETORNA TRUE SE O OBJETO DEVE SER ADICIONADO OU FALSE PARA NAO ADICIONAR NA FILTRAGEM
    boolean onFilter(Object obj);
}
