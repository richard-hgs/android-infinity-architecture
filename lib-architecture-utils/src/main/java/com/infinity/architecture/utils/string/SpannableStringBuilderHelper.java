package com.infinity.architecture.utils.string;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

public class SpannableStringBuilderHelper {
    private final String TAG = "SpanStrBuilderHelper";
    /*
     * SPAN EXAMPLES:
     * - Alignments:
     *      new AlignmentSpan.Standard(Alignment.ALIGN_OPPOSITE);
     * - TextSize:
     *      new AbsoluteSizeSpan(20, true);
     * - TextBold:
     *      new StyleSpan(android.graphics.Typeface.BOLD);
     * - TextColor:
     *      new ForegroundColorSpan(color)
     */

    private SpannableStringBuilder strbuilder;

    public SpannableStringBuilderHelper() {
        this.strbuilder = new SpannableStringBuilder();
    }

    public void addText(String someText, Object ...styles) {
        strbuilder.append(someText);

        int strBuilderLength = strbuilder.length();
        int someTextLength = someText.length();

        int startPos = strBuilderLength - someTextLength;
        int endPos = strBuilderLength;

        for (int i=0; i<styles.length; i++) {
            // Log.d(TAG, "strBuilder.length")
            strbuilder.setSpan(styles[i], startPos, endPos, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    public SpannableStringBuilder getSpan() {
        return this.strbuilder;
    }
}
