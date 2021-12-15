package com.d4viddf.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.icu.text.Transliterator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.core.view.MotionEventCompat;

import java.util.ArrayList;
import java.util.Random;

public class SnakeEngine extends SurfaceView implements
        Runnable {

    private Thread thread = null;
    private Context context;
    private int screenX;
    private int screenY;
    private int blockSize;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private int NUM_BLOCKS_WIDE = 30;
    private Paint paint;
    int numBlocksHigh;
    private Snake snake;
    private Apple apple;
    private boolean isPlaying;

    public SnakeEngine(MainActivity mainActivity, Point
            size) {
        super(mainActivity);
        context = mainActivity;
        screenX = size.x;
        screenY = size.y;
        blockSize = screenX / NUM_BLOCKS_WIDE;
        numBlocksHigh = screenY / blockSize;
        surfaceHolder = getHolder();
        paint = new Paint();
        newGame();
        draw();
        apple = new Apple();

    }

    @Override
    public void run() {
        while (isPlaying) {
            //va muy rapido, no es jugable
            try {
                thread.sleep(70);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update();
            draw();
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void newGame() {
        Position pos = new Position(NUM_BLOCKS_WIDE / 2, numBlocksHigh / 2);
        snake = new Snake(pos);
        draw();

    }

    public void spawnApple() {
        apple.setPosition(NUM_BLOCKS_WIDE - 10, numBlocksHigh - 10);
        Bitmap appleImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.manzana);
        Bitmap appImg = Bitmap.createScaledBitmap(appleImg, blockSize * 2, blockSize * 2, false);
        canvas.drawBitmap(appImg, (apple.getPosition().getPosX() * blockSize) - (blockSize / 2), (apple.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
    }

    private void eatApple() {
        if (apple != null) {
            if (apple.getPosition().getPosX() == snake.getPosition().getPosX()
                    && apple.getPosition().getPosY() == snake.getPosition().getPosY()) {
                snake.setSize(snake.getSize() + 1);
                Snake sp = new Snake();
                if (snake.cuerpo.isEmpty()) {
                    snake.addBodyPart(snake);
                } else {
                    snake.addBodyPart(snake.cuerpo.get(snake.cuerpo.size() - 1));
                }
                apple = new Apple();
                spawnApple();
            }
        }

    }


    private boolean isSnakeDeath() {
        return false;
    }

    public void setOirentetion(int i) {
        snake.setOrienteton(i);
    }

    public void update() {
        getOrientation();
        isScreenBorder();
        eatApple();
    }

    private void getOrientation() {
        if (snake.getOrienteton() == 0) {
            snake.getPosition().setPosY(snake.getPosition().getPosY() - 1);
        } else if (snake.getOrienteton() == 1) {
            snake.getPosition().setPosX(snake.getPosition().getPosX() + 1);
        } else if (snake.getOrienteton() == 2) {
            snake.getPosition().setPosY(snake.getPosition().getPosY() + 1);
        } else if (snake.getOrienteton() == 3) {
            snake.getPosition().setPosX(snake.getPosition().getPosX() - 1);
        }
        BodyPart bp;
        if (!snake.cuerpo.isEmpty()) {
            for (int i = snake.cuerpo.size() - 1; i >= 0; i--) {
                bp = snake.cuerpo.get(i);
                if (i == 0) {
                    bp.setPosition(snake.getPosition());
                    if (snake.getOrienteton() == 0) {
                        bp.getPosition().setPosY(snake.getPosition().getPosY() + 1);
                    } else if (snake.getOrienteton() == 1) {
                        bp.getPosition().setPosX(snake.getPosition().getPosX() - 1);
                    } else if (snake.getOrienteton() == 2) {
                        bp.getPosition().setPosY(snake.getPosition().getPosY() - 1);
                    } else if (snake.getOrienteton() == 3) {
                        bp.getPosition().setPosX(snake.getPosition().getPosX() + 1);
                    }
                } else {
                    bp.setPosition(snake.cuerpo.get(i - 1).position);
                }
            }
        }
    }


    private void isScreenBorder() {
        if (snake.getPosition().getPosX() >= NUM_BLOCKS_WIDE) {
            snake.setPosition(new Position(0, snake.getPosition().getPosY()));
        } else if (snake.getPosition().getPosY() >= numBlocksHigh) {
            snake.setPosition(new Position(snake.getPosition().PosX, 0));
        } else if (snake.getPosition().getPosX() < 0) {
            snake.setPosition(new Position(NUM_BLOCKS_WIDE, snake.getPosition().getPosY()));
        } else if (snake.getPosition().getPosY() < 0) {
            snake.setPosition(new Position(snake.getPosition().PosX, numBlocksHigh));
        }

    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 10, 60, 100));

            for (BodyPart snake1 : snake.cuerpo) {

                paint.setColor(Color.argb(255,118,140,48));
                canvas.drawRect(snake1.getPosition().getPosX() * blockSize, (snake1.getPosition().getPosY() * blockSize), (snake1.getPosition().getPosX() * blockSize) + blockSize, (snake1.getPosition().getPosY() * blockSize) + blockSize, paint);

            }

            Bitmap snakeHead = BitmapFactory.decodeResource(context.getResources(), R.drawable.snake_head);
            Bitmap snakeHeadSized = Bitmap.createScaledBitmap(snakeHead, blockSize * 2, blockSize * 2, false);
            if (snake.getOrienteton() == 2){
                canvas.drawBitmap(rotateBitmap(snakeHeadSized,0), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            }
            else if (snake.getOrienteton() == 3){
                canvas.drawBitmap(rotateBitmap(snakeHeadSized,90), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            }
            else if (snake.getOrienteton() == 0){
                canvas.drawBitmap(rotateBitmap(snakeHeadSized,180), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            } else if (snake.getOrienteton() == 1){
                canvas.drawBitmap(rotateBitmap(snakeHeadSized,-90), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            }
            /*paint.setColor(snake.getColor());
            canvas.drawRect(snake.getPosition().getPosX() * blockSize, (snake.getPosition().getPosY() * blockSize), (snake.getPosition().getPosX() * blockSize) + blockSize, (snake.getPosition().getPosY() * blockSize) + blockSize, paint);
            */

            spawnApple();
            paint.setColor(Color.WHITE);
            paint.setTextSize(60);
            paint.setFakeBoldText(true);

            canvas.drawText("Puntuación: " + snake.getSize(), screenX / 3, 3 * blockSize, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);


        }
    }

    float firstX_point, firstY_point;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                firstX_point = event.getRawX();
                firstY_point = event.getRawY();

                break;

            case MotionEvent.ACTION_UP:

                float finalX = event.getRawX();
                float finalY = event.getRawY();

                int distanceX = (int) (finalX - firstX_point);
                int distanceY = (int) (finalY - firstY_point);

                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    if ((firstX_point < finalX)) {
                        if (snake.cuerpo.isEmpty()) {
                            setOirentetion(1);
                        } else if (snake.getOrienteton() != 3)
                            setOirentetion(1);
                    } else {
                        if (snake.cuerpo.isEmpty()) {
                            setOirentetion(3);
                        } else if (snake.getOrienteton() != 1)
                            setOirentetion(3);
                    }
                } else {
                    if ((firstY_point < finalY)) {
                        if (snake.cuerpo.isEmpty()) {
                            setOirentetion(2);
                        } else if (snake.getOrienteton() != 0)
                            setOirentetion(2);
                    } else {
                        if (snake.cuerpo.isEmpty()) {
                            setOirentetion(0);
                        } else if (snake.getOrienteton() != 2)
                            setOirentetion(0);
                    }
                }
                break;
        }
        return true;
    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);

        return rotatedBitmap;
    }
}
