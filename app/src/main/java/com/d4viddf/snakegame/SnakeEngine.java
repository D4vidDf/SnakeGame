package com.d4viddf.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private ArrayList<Snake> cuerpo = new ArrayList<>();

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
                thread.sleep(100);
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
        apple.setPosition(NUM_BLOCKS_WIDE, numBlocksHigh);
        paint.setColor(apple.getColor());
        canvas.drawRect(apple.getPosition().getPosX() * blockSize, (apple.getPosition().getPosY() * blockSize), (apple.getPosition().getPosX() * blockSize) + blockSize, (apple.getPosition().getPosY() * blockSize) + blockSize, paint);
    }

    private void eatApple() {
        if (apple != null) {
            if (apple.getPosition().getPosX() == snake.getPosition().getPosX()
                    && apple.getPosition().getPosY() == snake.getPosition().getPosY()) {
                snake.setSize(snake.getSize() + 1);
                Snake sp = new Snake();
                if (snake.getOrienteton() == 0) {
                    sp.setPosition(apple.getPosition());
                    sp.getPosition().setPosY(sp.getPosition().getPosY() - 1);
                } else if (snake.getOrienteton() == 1) {
                    sp.setPosition(apple.getPosition());
                    sp.getPosition().setPosX(sp.getPosition().getPosX() + 1);
                } else if (snake.getOrienteton() == 2) {
                    sp.setPosition(apple.getPosition());
                    sp.getPosition().setPosY(sp.getPosition().getPosY() + 1);
                } else if (snake.getOrienteton() == 3) {
                    sp.setPosition(apple.getPosition());
                    sp.getPosition().setPosX(sp.getPosition().getPosX() - 1);
                }

                addCuerpo(sp);
                apple = new Apple();
            }
        }

    }

    private void addCuerpo(Snake snake1) {
        cuerpo.add(snake1);
        Log.i("cuerpo",String.valueOf(cuerpo.size()));
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

        ArrayList<Snake> cuerp = new ArrayList<>();
        int i = 0;
        for (Snake snake1 : cuerpo) {
            if (i == 0){
                Position pos = null;
                if (snake.getOrienteton() == 0) {
                     pos = new Position(snake.getPosition().getPosX(),snake.getPosition().getPosY()+1);

                } else if (snake.getOrienteton() == 1) {
                    pos = new Position(snake.getPosition().getPosX()-1,snake.getPosition().getPosY());
                    //snake.getPosition().setPosX(snake.getPosition().getPosX() + 1);
                } else if (snake.getOrienteton() == 2) {
                    pos = new Position(snake.getPosition().getPosX(),snake.getPosition().getPosY()-1);
                    //snake.getPosition().setPosY(snake.getPosition().getPosY() + 1);
                } else if (snake.getOrienteton() == 3) {
                    pos = new Position(snake.getPosition().getPosX()+1,snake.getPosition().getPosY());
                    //snake.getPosition().setPosX(snake.getPosition().getPosX() - 1);
                }

                snake1.setPosition(pos);
                cuerp.add(snake1);
                cuerpo = cuerp;

            }
            else {
                Position pos = null;
                if (snake.getOrienteton() == 0) {
                    pos = new Position(cuerpo.get(i-1).getPosition().getPosX(),cuerpo.get(i-1).getPosition().getPosY()+1);

                } else if (snake.getOrienteton() == 1) {
                    pos = new Position(cuerpo.get(i-1).getPosition().getPosX()-1,cuerpo.get(i-1).getPosition().getPosY());
                    //snake.getPosition().setPosX(snake.getPosition().getPosX() + 1);
                } else if (snake.getOrienteton() == 2) {
                    pos = new Position(cuerpo.get(i-1).getPosition().getPosX(),cuerpo.get(i-1).getPosition().getPosY()-1);
                    //snake.getPosition().setPosY(snake.getPosition().getPosY() + 1);
                } else if (snake.getOrienteton() == 3) {
                    pos = new Position(cuerpo.get(i-1).getPosition().getPosX()+1,cuerpo.get(i-1).getPosition().getPosY());
                    //snake.getPosition().setPosX(snake.getPosition().getPosX() - 1);
                }
                snake1.setPosition(pos);
                cuerp.add(snake1);


            }
            i++;

        }
        cuerpo = cuerp;
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
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(snake.getColor());
            canvas.drawRect(snake.getPosition().getPosX() * blockSize, (snake.getPosition().getPosY() * blockSize), (snake.getPosition().getPosX() * blockSize) + blockSize, (snake.getPosition().getPosY() * blockSize) + blockSize, paint);
            for (Snake snake1 : cuerpo) {
                canvas.drawRect(snake1.getPosition().getPosX() * blockSize, (snake1.getPosition().getPosY() * blockSize), (snake1.getPosition().getPosX() * blockSize) + blockSize, (snake1.getPosition().getPosY() * blockSize) + blockSize, paint);

            }
            spawnApple();
            paint.setColor(Color.WHITE);
            paint.setTextSize(60);
            paint.setFakeBoldText(true);

            canvas.drawText("Puntuación: " + snake.getSize(), screenX / 3, 4 * blockSize, paint);
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
                        setOirentetion(1);
                    } else {
                        setOirentetion(3);
                    }
                } else {
                    if ((firstY_point < finalY)) {
                        setOirentetion(2);
                    } else {
                        setOirentetion(0);
                    }
                }
                break;
        }
        return true;
    }
}
