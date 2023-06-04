package com.danny.xcamera;

import androidx.camera.core.ImageCapture;

public class CameraXConstants {
    public static final String FLASH_MODE = "FLASH_MODE";
    public static final String IS_SAVE_SD = "IS_SAVE_SD";
    public static final String ADD_WATER = "ADD_WATER";
    public static final String NEED_LOCATION = "NEED_LOCATION";

    interface FlashMode {
        int FLASH_MODE_AUTO = ImageCapture.FLASH_MODE_AUTO;
        int FLASH_MODE_ON = ImageCapture.FLASH_MODE_ON;
        int FLASH_MODE_OFF = ImageCapture.FLASH_MODE_OFF;
        int FLASH_MODE_ALWAYS_ON = 3;
    }
}
