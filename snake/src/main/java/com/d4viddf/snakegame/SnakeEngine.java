package com.d4viddf.snakegame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class SnakeEngine extends SurfaceView implements
        Runnable {
    public MediaPlayer mp;
    private Thread thread = null;
    private final Context context;
    private final int screenX;
    private final int screenY;
    private final int blockSize;
    private Canvas canvas;
    private final SurfaceHolder surfaceHolder;
    private final int NUM_BLOCKS_WIDE = 20;
    private final Paint paint;
    int numBlocksHigh;
    private Snake snake;
    private Apple apple;
    private boolean isPlaying, dead = false, onpause = false, inicio = true, intro = true;
    private final String score;
    private final String resume;
    private final String control;
    private final String desc_control;
    private final String strat;
    private final String new_game;
    private final String resume_desc;

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
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (inicio) {
                welcome();
            } else if (intro) {
                introShow();
            } else if (onpause) {
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
            textPaint.setTextSize(20);
            textPaint.setFakeBoldText(true);
            canvas.drawText(control, screenX / 2, screenY / 2, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(22);
            textPaint.setFakeBoldText(true);
            canvas.drawText(desc_control, screenX / 2, screenY / 2 + screenY / 8, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.press);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 4, blockSize * 4, false);
            canvas.drawBitmap(logoSized, screenX / 3 + screenX / 14, screenY / 6, img);
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
            textPaint.setTextSize(20);
            textPaint.setFakeBoldText(true);
            canvas.drawText("S   N   A   K  " +
                    " E", screenX / 2, screenY / 2 + screenY / 7, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(22);
            textPaint.setFakeBoldText(true);
            canvas.drawText(strat, screenX / 2, screenY / 2 + screenY / 4, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 16, blockSize * 16, false);
            canvas.drawBitmap(logoSized, screenX / 9, screenY / 44, img);
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
            textPaint.setTextSize(26);
            textPaint.setFakeBoldText(true);
            canvas.drawText("G A M E  O V E R", screenX / 2, screenY / 2+screenY/8, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(20);
            textPaint.setFakeBoldText(true);
            canvas.drawText(new_game, screenX / 2, screenY / 2 + screenY / 4, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.skull);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 6, blockSize * 6, false);
            canvas.drawBitmap(logoSized, screenX / 4 + screenX / 11, screenY / 6, img);
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
            textPaint.setTextSize(20);
            textPaint.setFakeBoldText(true);
            canvas.drawText(resume, screenX / 2, screenY / 2 + screenY / 6, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(22);
            textPaint.setFakeBoldText(true);
            canvas.drawText(resume_desc, screenX / 2, screenY / 2 + screenY / 3, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.gamepad);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 8, blockSize * 8, false);
            canvas.drawBitmap(logoSized, screenX / 4 + screenX / 16, screenY / 6, img);
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

    private void isSnakeDeath() {
        for (BodyPart bodyPart : snake.cuerpo) {
            if (bodyPart.getPosition().getPosX() == snake.getPosition().getPosX()
                    && bodyPart.getPosition().getPosY() == snake.getPosition().getPosY()) {
                dead = true;
                mp.pause();
                return;
            }
        }
        dead = false;
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
        textPaint.setTextSize(24);
        textPaint.setFakeBoldText(true);
        canvas.drawText(score + snake.getSize(),
                screenX / 2,
                2 * blockSize + blockSize / 8,
                textPaint);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                if (dead) {
                    newGame();
                    dead = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (inicio) {
                    inicio = false;

                } else if (intro) {
                    intro = false;
                    newGame();
                } else {
                    if (onpause) {
                        onpause = false;

                    } else {
                        if (snake.getOrienteton() == 0) {
                            setOirentetion(3);
                        } else if (snake.getOrienteton() == 1) {
                            setOirentetion(0);
                        } else if (snake.getOrienteton() == 2) {
                            setOirentetion(1);
                        } else {
                            setOirentetion(2);
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

        return Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
    }
}
