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
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
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

    public MediaPlayer mp;
    private Thread thread = null;
    private Context context;
    private int screenX;
    private int screenY;
    private int blockSize;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private int NUM_BLOCKS_WIDE = 25;
    private Paint paint;
    int numBlocksHigh;
    private Snake snake;
    private Apple apple;
    private boolean isPlaying, dead = false, onpause = false, inicio = true, intro = true;
    private String score, resume, control, desc_control, strat, new_game, resume_desc;

    public SnakeEngine(MainActivity mainActivity, Point
            size, MediaPlayer mediaPlayer) {
        super(mainActivity);
        this.mp = mediaPlayer;
        context = mainActivity;

        score = (String) context.getResources().getText(R.string.point);
        resume = (String) context.getResources().getText(R.string.resume);
        resume_desc = (String) context.getResources().getText(R.string.resume_desc);
        new_game = (String) context.getResources().getText(R.string.new_game);
        strat = (String) context.getResources().getText(R.string.strat);
        control = (String) context.getResources().getText(R.string.control);
        desc_control = (String) context.getResources().getText(R.string.desc_control);

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
                thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (inicio) {
                welcome();
            } else if (intro){
                introShow();
            }
            else if (onpause) {
                pauseMenu();
            } else {
                if (!dead) {
                    update();
                    draw();
                    if (!mp.isPlaying()) mp.start();
                } else {
                    endgame();
                }
            }

        }

    }

    private void introShow() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 10, 60, 100));
            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(100);
            textPaint.setFakeBoldText(true);
            canvas.drawText(control, screenX / 2, screenY / 2, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(45);
            textPaint.setFakeBoldText(true);
            canvas.drawText(desc_control, screenX / 2, screenY / 2 + screenY / 8, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 10, blockSize * 10, false);
            canvas.drawBitmap(logoSized, screenX / 4+ screenX/14, screenY / 6, img);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void welcome() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 10, 60, 100));
            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(100);
            textPaint.setFakeBoldText(true);
            canvas.drawText("S   N   A   K  " +
                    " E", screenX / 2, screenY / 2, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(45);
            textPaint.setFakeBoldText(true);
            canvas.drawText(strat, screenX / 2, screenY / 2 + screenY / 3, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 20, blockSize * 20, false);
            canvas.drawBitmap(logoSized, screenX / 8, screenY / 9, img);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void endgame() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 10, 60, 100));
            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(80);
            textPaint.setFakeBoldText(true);
            canvas.drawText("G A M E  O V E R", screenX / 2, screenY / 2, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(45);
            textPaint.setFakeBoldText(true);
            canvas.drawText(new_game, screenX / 2, screenY / 2 + screenY / 3, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.skull);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 10, blockSize * 10, false);
            canvas.drawBitmap(logoSized, screenX / 4+ screenX/14, screenY / 6, img);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void pauseMenu() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 10, 60, 100));
            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(80);
            textPaint.setFakeBoldText(true);
            canvas.drawText(resume, screenX / 2, screenY / 2, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(45);
            textPaint.setFakeBoldText(true);
            canvas.drawText(resume_desc, screenX / 2, screenY / 2 + screenY / 3, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.gamepad);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 10, blockSize * 10, false);
            canvas.drawBitmap(logoSized, screenX / 4+ screenX/14, screenY / 6, img);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {

        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mp.pause();
        pauseMenu();
        onpause = true;

    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void newGame() {
        Position pos = new Position(NUM_BLOCKS_WIDE / 2, numBlocksHigh / 2);
        snake = new Snake(pos);
        onpause = false;
        draw();

    }

    public void spawnApple() {
        apple.setPosition(NUM_BLOCKS_WIDE - 10, numBlocksHigh - 10);
        Bitmap appleImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
        Bitmap appImg = Bitmap.createScaledBitmap(appleImg, blockSize + 12, blockSize + 12, false);
        canvas.drawBitmap(appImg, (apple.getPosition().getPosX() * blockSize) - (blockSize / 3), (apple.getPosition().getPosY() * blockSize) - (blockSize / 3), paint);
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
        for (BodyPart bodyPart : snake.cuerpo) {
            if (bodyPart.getPosition().getPosX() == snake.getPosition().getPosX()
                    && bodyPart.getPosition().getPosY() == snake.getPosition().getPosY()) {
                dead = true;
                mp.pause();
                return true;
            }
        }
        dead = false;
        return false;
    }

    public void setOirentetion(int i) {
        snake.setOrienteton(i);
    }

    public void update() {
        getOrientation();
        isScreenBorder();
        isSnakeDeath();
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
            int i = 0;
            for (BodyPart snake1 : snake.cuerpo) {

                if (i % 2 == 0) {
                    paint.setColor(Color.argb(255, 80, 110, 48));
                } else {
                    paint.setColor(Color.argb(255, 118, 140, 48));
                }
                canvas.drawRect(snake1.getPosition().getPosX() * blockSize, (snake1.getPosition().getPosY() * blockSize), (snake1.getPosition().getPosX() * blockSize) + blockSize, (snake1.getPosition().getPosY() * blockSize) + blockSize, paint);

                i++;
            }

            Bitmap snakeHead = BitmapFactory.decodeResource(context.getResources(), R.drawable.snake_head);
            Bitmap snakeHeadSized = Bitmap.createScaledBitmap(snakeHead, blockSize * 2, blockSize * 2, false);
            if (snake.getOrienteton() == 2) {
                canvas.drawBitmap(rotateBitmap(snakeHeadSized, 0), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            } else if (snake.getOrienteton() == 3) {
                canvas.drawBitmap(rotateBitmap(snakeHeadSized, 90), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            } else if (snake.getOrienteton() == 0) {
                canvas.drawBitmap(rotateBitmap(snakeHeadSized, 180), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            } else if (snake.getOrienteton() == 1) {
                canvas.drawBitmap(rotateBitmap(snakeHeadSized, -90), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            }
            spawnApple();
            puntuacion();
            surfaceHolder.unlockCanvasAndPost(canvas);


        }
    }

    private void puntuacion() {
        Paint textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(80);
        textPaint.setFakeBoldText(true);
        canvas.drawText(score + snake.getSize(),
                screenX /2,
                2 * blockSize + blockSize/8,
                textPaint);
    }

    float firstX_point, firstY_point;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {


            case MotionEvent.ACTION_DOWN:
                if (dead) {
                    newGame();
                    dead = false;
                }
                firstX_point = event.getRawX();
                firstY_point = event.getRawY();

                break;

            case MotionEvent.ACTION_UP:

                float finalX = event.getRawX();
                float finalY = event.getRawY();

                int distanceX = (int) (finalX - firstX_point);
                int distanceY = (int) (finalY - firstY_point);
                if (inicio) {
                    inicio = false;

                }
                else if (intro){
                    intro = false;
                    newGame();
                }
                else {
                    if (onpause) {
                        onpause = false;

                    } else {

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
