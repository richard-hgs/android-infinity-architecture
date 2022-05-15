package com.infinity.architecture.utils.scroll;

import android.view.View;
import android.widget.ScrollView;

public class ScrollUtils {

    /**
     * RETORNA A POSICAO MAXIMA DE SCROLL VERTICAL
     * @param scrollView    SCROLL A RECEBER A POSICAO MAXIMA
     * @return              POSICAO MÁXIMA PERMITIDA DE SCROLL
     */
    public static int getMaxScrollViewPos(ScrollView scrollView) {
        View firstDirectChild = scrollView.getChildAt(0);
        if (firstDirectChild != null && scrollView.getHeight() > 0 && firstDirectChild.getHeight() > scrollView.getHeight()) {
            return firstDirectChild.getHeight() - scrollView.getHeight();
        } else {
            return 0;
        }
    }
}
