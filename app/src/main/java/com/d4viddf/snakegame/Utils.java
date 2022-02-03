package com.d4viddf.snakegame;

import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class Utils {

    public static Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
        return rotatedBitmap;
    }

    public void vibrateEatApple(Context context){
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(120, VibrationEffect.EFFECT_TICK));
        } else {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(120);
        }
    }

    public void vibrateButtonApple(Context context){
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(80, VibrationEffect.EFFECT_TICK));
        } else {
            ((Vibrator) context.getSystemService(VIBRATOR_SERVICE)).vibrate(80);
        }
    }

}
