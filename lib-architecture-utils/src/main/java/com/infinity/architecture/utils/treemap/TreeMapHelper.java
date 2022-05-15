package com.infinity.architecture.utils.treemap;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class TreeMapHelper<T> extends TreeMap<T, T> {

    private Map<T, T> map;

    private Comparator<T> comparator;

    // Construtor
    public TreeMapHelper(Map<T, T> map) {
        this.map = map;
    }

    // Seta o parametro de validacao de ordem da lista
    public TreeMapHelper<T> setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
        return this;
    }

    // Retorna a lista ordenada e organizada na ordem escolhida
    public Map<T, T> getSortedMap() {
        Map<T, T> sortedMap = new TreeMap<T, T>(comparator);
        sortedMap.putAll(map);
        return sortedMap;
    }
}
