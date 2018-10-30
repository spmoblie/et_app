package com.elite.selfhelp.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by xuzz on 2018/5/3.
 */

public class ZxingUtils {

    public static Bitmap creatBarcode(String contents, int desiredWidth, int desiredHeight) {
        return encodeAsBitmap(contents, BarcodeFormat.CODE_128, desiredWidth, desiredHeight);
    }

    private static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight) {
        final int WHITE = 0xFFF6F6F6;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(contents, format, desiredWidth, desiredHeight, null);
        } catch (WriterException e) {
            ExceptionUtils.handle(e);
        }

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
