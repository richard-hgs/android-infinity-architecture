package com.infinity.architecture.base.models.ui;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infinity.architecture.utils.file.FileHelper;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

public class EditPictureInfo {
    private Uri sourceUri;
    private Uri destUri;
    private float ratioX;
    private float ratioY;
    @UCropActivity.GestureTypes
    private int tabScale;
    @UCropActivity.GestureTypes
    private int tabRotate;
    @UCropActivity.GestureTypes
    private int tabAspectRatio;

    private EditPictureInfo() {}

    public static EditPictureInfo getInstance(
        @Nullable Uri sourceUri,
        @Nullable Uri destinationUri
    ) {
        return getInstance(
            sourceUri,
            destinationUri,
            0,
            0,
            UCropActivity.SCALE,
            UCropActivity.ROTATE,
            UCropActivity.ALL
        );
    }

    public static EditPictureInfo getInstance(
        float ratioX,
        float ratioY,
        boolean allowScale,
        boolean allowRotate,
        boolean allowRatio
    ) {
        return getInstance(
            null,
            null,
            ratioX,
            ratioY,
            allowScale ? UCropActivity.SCALE : UCropActivity.NONE,
            allowRotate ? UCropActivity.ROTATE : UCropActivity.NONE,
            allowRatio ? UCropActivity.ALL : UCropActivity.NONE
        );
    }

    public static EditPictureInfo getInstance(
        @Nullable Uri sourceUri,
        @Nullable Uri destinationUri,
        float ratioX,
        float ratioY,
        boolean allowScale,
        boolean allowRotate,
        boolean allowRatio
    ) {
        return getInstance(
            sourceUri,
            destinationUri,
            ratioX,
            ratioY,
            allowScale ? UCropActivity.SCALE : UCropActivity.NONE,
            allowRotate ? UCropActivity.ROTATE : UCropActivity.NONE,
            allowRatio ? UCropActivity.ALL : UCropActivity.NONE
        );
    }

    public static EditPictureInfo getInstance(
        @Nullable Uri sourceUri,
        @Nullable Uri destinationUri,
        float ratioX,
        float ratioY,
        @UCropActivity.GestureTypes int tabScale,
        @UCropActivity.GestureTypes int tabRotate,
        @UCropActivity.GestureTypes int tabAspectRatio
    ) {
        EditPictureInfo photoEditorInfo = new EditPictureInfo();
        photoEditorInfo.sourceUri = sourceUri;
        photoEditorInfo.destUri = destinationUri;
        photoEditorInfo.ratioX = ratioX;
        photoEditorInfo.ratioY = ratioY;
        photoEditorInfo.tabScale = tabScale;
        photoEditorInfo.tabRotate = tabRotate;
        photoEditorInfo.tabAspectRatio = tabAspectRatio;
        return photoEditorInfo;
    }

    @NonNull
    public UCrop getuCrop() {
        return UCrop
            .of(sourceUri, destUri)
            .withAspectRatio(ratioX, ratioY);
    }

    @NonNull
    public UCrop.Options getuCropOptions() {
        UCrop.Options uCropOptions = new UCrop.Options();
        uCropOptions.setToolbarTitle("Editar Imagem");
        uCropOptions.setAllowedGestures(tabScale, tabRotate, tabAspectRatio);
        return uCropOptions;
    }

    public Uri getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(Uri sourceUri) {
        this.sourceUri = sourceUri;
    }

    public Uri getDestUri() {
        return destUri;
    }

    public void setDestUri(Uri destUri) {
        this.destUri = destUri;
    }

    @Nullable
    public String getSourcePath() {
        return FileHelper.getUriPath(sourceUri);
    }

    @Nullable
    public String getDestinationPath() {
        return FileHelper.getUriPath(destUri);
    }
}
