package com.d4viddf.snakegame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Utils {

    public static Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
        return rotatedBitmap;
    }
    private void backgroundPaint() {

        /*for (int i = 0; i < numBlocksHigh; i++) {
            for (int ii = 0; ii < NUM_BLOCKS_WIDE; ii++) {
                if (i % 2 != 0) {
                    if (ii % 2 == 0) {
                        Paint paint2 = new Paint();
                        paint2.setColor(Color.argb(60, 67, 98, 140));
                        canvas.drawRect(ii * blockSize, (i * blockSize), (ii * blockSize) + blockSize, (i * blockSize) + blockSize, paint2);
                    }
                } else if (ii % 2 != 0) {
                    Paint paint2 = new Paint();
                    paint2.setColor(Color.argb(60, 67, 98, 140));
                    canvas.drawRect(ii * blockSize, (i * blockSize), (ii * blockSize) + blockSize, (i * blockSize) + blockSize, paint2);

                }

            }
        }*/

    }

}
