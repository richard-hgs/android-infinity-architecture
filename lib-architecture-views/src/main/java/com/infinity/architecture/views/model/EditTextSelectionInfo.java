package com.infinity.architecture.views.model;


import androidx.annotation.NonNull;

import com.infinity.architecture.views.enums.EnEditTextSelection;

public class EditTextSelectionInfo {

    private EnEditTextSelection enEditTextSelection;
    private Integer             startOffset;
    private Integer             endOffset;

    private EditTextSelectionInfo() {

    }

    @NonNull
    public static EditTextSelectionInfo caretPos(int caretPos) {
        EditTextSelectionInfo editTextSelectionInfo = new EditTextSelectionInfo();
        editTextSelectionInfo.enEditTextSelection = EnEditTextSelection.CARET_POS;
        editTextSelectionInfo.startOffset = caretPos;
        return editTextSelectionInfo;
    }

    @NonNull
    public static EditTextSelectionInfo selectAll() {
        EditTextSelectionInfo editTextSelectionInfo = new EditTextSelectionInfo();
        editTextSelectionInfo.enEditTextSelection = EnEditTextSelection.ALL;
        return editTextSelectionInfo;
    }

    @NonNull
    public static EditTextSelectionInfo deselectAll() {
        EditTextSelectionInfo editTextSelectionInfo = new EditTextSelectionInfo();
        editTextSelectionInfo.enEditTextSelection = EnEditTextSelection.DESELECT;
        return editTextSelectionInfo;
    }

    @NonNull
    public static EditTextSelectionInfo selectCustom(int startOffset, int endOffset) {
        EditTextSelectionInfo editTextSelectionInfo = new EditTextSelectionInfo();
        editTextSelectionInfo.enEditTextSelection = EnEditTextSelection.CUSTOM;
        editTextSelectionInfo.startOffset = startOffset;
        editTextSelectionInfo.endOffset = endOffset;
        return editTextSelectionInfo;
    }

    @NonNull
    public static EditTextSelectionInfo moveCaretEnd() {
        EditTextSelectionInfo editTextSelectionInfo = new EditTextSelectionInfo();
        editTextSelectionInfo.enEditTextSelection = EnEditTextSelection.MOVE_CARET_TO_END;
        return editTextSelectionInfo;
    }

    @NonNull
    public EnEditTextSelection getEnEditTextSelection() {
        return enEditTextSelection;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public Integer getEndOffset() {
        return endOffset;
    }
}
