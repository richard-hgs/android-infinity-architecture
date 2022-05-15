package com.infinity.architecture.base.models.ui;

import androidx.annotation.NonNull;

public class TakePictureInfo {

    private String imgPath;

    private TakePictureInfo() {}

    public static TakePictureInfo getInstance(@NonNull String imagePathInFilesDir) {
        TakePictureInfo takePictureInfo = new TakePictureInfo();
        takePictureInfo.imgPath = imagePathInFilesDir;
        return takePictureInfo;
    }

    @NonNull
    public String getImgPath() {
        return imgPath;
    }
}
