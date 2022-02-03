package com.d4viddf.snakegame;

import static com.d4viddf.snakegame.R.drawable;
import static com.d4viddf.snakegame.R.string;
import static com.d4viddf.snakegame.R.string.achievement_addicted_to_the_game;
import static com.d4viddf.snakegame.R.string.achievement_apple_lover;
import static com.d4viddf.snakegame.R.string.achievement_apprentice;
import static com.d4viddf.snakegame.R.string.achievement_beginner;
import static com.d4viddf.snakegame.R.string.achievement_learning_to_play;
import static com.d4viddf.snakegame.R.string.achievement_master;
import static com.d4viddf.snakegame.R.string.achievement_snake_charmer;
import static com.d4viddf.snakegame.R.string.leaderboard_classification;
import static com.d4viddf.snakegame.R.string.maxpoint;
import static com.d4viddf.snakegame.R.string.point;
import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;

import java.util.Objects;

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
    private final int NUM_BLOCKS_WIDE = 25;
    private final Paint paint;
    int numBlocksHigh;
    private Snake snake;
    private Apple apple;
    private boolean isPlaying;
    private boolean dead = false;
    private boolean onpause = false;
    private final String score;
    private final String resume;
    private final String new_game;
    private final String resume_desc;
    private final String maxscore;
    private final Point size;
    private PrefManager prefManager;


    public SnakeEngine(MainActivity mainActivity, Point
            size, MediaPlayer mediaPlayer) {
        super(mainActivity);
        this.mp = mediaPlayer;
        this.size = size;
        this.mp.setVolume(0.3f, 0.2f);

        prefManager = new PrefManager(getContext());
        context = mainActivity;
        maxscore = (String) context.getResources().getText(maxpoint);
        score = (String) context.getResources().getText(point);
        resume = (String) context.getResources().getText(string.resume);
        resume_desc = (String) context.getResources().getText(string.resume_desc);
        new_game = (String) context.getResources().getText(string.new_game);
        mp.start();
        screenX = size.x;
        screenY = size.y;
        blockSize = screenX / NUM_BLOCKS_WIDE;
        numBlocksHigh = screenY / blockSize;
        surfaceHolder = getHolder();
        paint = new Paint();
        draw();
        newGame();
        apple = new Apple();
        if (!prefManager.isUserChild() && GoogleSignIn.getLastSignedInAccount(getContext()) != null) {
            GamesClient gamesClient = Games.getGamesClient(context.getApplicationContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context.getApplicationContext())));
            gamesClient.setViewForPopups(findViewById(android.R.id.content));
            gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        }
    }

    @Override
    public void run() {
        while (isPlaying) {
            try {
                sleep(24);
                if (!onpause) {
                    if (!dead) {
                        draw();
                        update();
                        if (!mp.isPlaying()) mp.start();
                    } else endgame();
                } else pauseMenu();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    private void endgame() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 10, 60, 100));
            Paint textPaintMax = new Paint();
            textPaintMax.setTextAlign(Paint.Align.CENTER);
            textPaintMax.setColor(Color.WHITE);
            if (size.x <= 1000) textPaintMax.setTextSize(30);
            else textPaintMax.setTextSize(60);
            textPaintMax.setFakeBoldText(true);
            canvas.drawText(maxscore + prefManager.getMaxPoint(),
                    screenX / 2,
                    4 * blockSize - blockSize / 8,
                    textPaintMax);
            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(size.x < 1000 ? 40 : 80);
            textPaint.setFakeBoldText(true);
            canvas.drawText("G A M E  O V E R", screenX / 2, screenY / 2, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(size.x < 1000 ? 20 : 45);
            textPaint.setFakeBoldText(true);
            canvas.drawText(new_game, screenX / 2, screenY / 2 + screenY / 3, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), drawable.skull);
            Bitmap logoSized = Bitmap.createScaledBitmap(logo, blockSize * 10, blockSize * 10, false);
            canvas.drawBitmap(logoSized, screenX / 4 + screenX / 14, screenY / 6, img);
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
            textPaint.setTextSize(size.x < 1000 ? 40 : 80);
            textPaint.setFakeBoldText(true);
            canvas.drawText(resume, screenX / 2, screenY / 2, textPaint);
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(size.x < 1000 ? 20 : 45);
            textPaint.setFakeBoldText(true);
            canvas.drawText(resume_desc, screenX / 2, screenY / 2 + screenY / 3, textPaint);
            Paint img = new Paint();
            img.setTextAlign(Paint.Align.CENTER);
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), drawable.gamepad);
            Bitmap logoSized;
            if (size.x < 1000) {
                logoSized = Bitmap.createScaledBitmap(logo, blockSize * 8, blockSize * 8, false);
                canvas.drawBitmap(logoSized, screenX / 4 + screenX / 12, screenY / 4, img);
            } else {
                logoSized = Bitmap.createScaledBitmap(logo, blockSize * 10, blockSize * 10, false);
                canvas.drawBitmap(logoSized, screenX / 4 + screenX / 14, screenY / 6, img);
            }
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
        draw();
        Position pos = new Position(NUM_BLOCKS_WIDE / 2, numBlocksHigh / 2);
        snake = new Snake(pos);
        onpause = false;
        if (!prefManager.isUserChild() && GoogleSignIn.getLastSignedInAccount(getContext()) != null) {
            Games.getAchievementsClient(context.getApplicationContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context)))
                    .increment(context.getString(achievement_learning_to_play), 1);
            Games.getAchievementsClient(context.getApplicationContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context)))
                    .increment(context.getString(achievement_addicted_to_the_game), 1);

        }
    }

    public void spawnApple() {
        apple.setPosition(NUM_BLOCKS_WIDE - 10, numBlocksHigh - 10);
        Bitmap appleImg = BitmapFactory.decodeResource(context.getResources(), drawable.apple);
        Bitmap appImg = Bitmap.createScaledBitmap(appleImg, blockSize + 12, blockSize + 12, false);
        canvas.drawBitmap(appImg, (apple.getPosition().getPosX() * blockSize) - (blockSize / 3), (apple.getPosition().getPosY() * blockSize) - (blockSize / 3), paint);
    }

    private void eatApple() {
        if (apple != null) {
            if (apple.getPosition().getPosX() == snake.getPosition().getPosX()
                    && apple.getPosition().getPosY() == snake.getPosition().getPosY()) {
                snake.setSize(snake.getSize() + 1);
                snake.addBodyPart(!snake.cuerpo.isEmpty() ? snake.cuerpo.get(snake.cuerpo.size() - 1) : snake);
                apple = new Apple();
                new Utils().vibrateEatApple(getContext());
                spawnApple();
                logros();
                maxPuntuacion();

            }
        }
    }

    private void maxPuntuacion() {
        if (prefManager.getMaxPoint() < snake.getSize()) {
            prefManager.setMaxPoint(snake.getSize());
        }
    }

    private void logros() {
        if (!prefManager.isUserChild() && GoogleSignIn.getLastSignedInAccount(getContext()) != null) {
            Games.getAchievementsClient(getContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context)))
                    .unlock(context.getString(achievement_apple_lover));
            Games.getAchievementsClient(context.getApplicationContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context)))
                    .increment(context.getString(achievement_beginner), 1);
            Games.getAchievementsClient(context.getApplicationContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context)))
                    .increment(context.getString(achievement_apprentice), 1);
            Games.getAchievementsClient(context.getApplicationContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context)))
                    .increment(context.getString(achievement_master), 1);

            if (snake.getSize() == 50) {
                Games.getAchievementsClient(context.getApplicationContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context)))
                        .unlock(context.getString(achievement_snake_charmer));
            }

            Games.getLeaderboardsClient(context.getApplicationContext(), Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(context.getApplicationContext())))
                    .submitScore(context.getString(leaderboard_classification), snake.getSize());
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
        switch (snake.getOrienteton()) {
            case 0:
                snake.getPosition().setPosY(snake.getPosition().getPosY() - 1);
                break;
            case 1:
                snake.getPosition().setPosX(snake.getPosition().getPosX() + 1);
                break;
            case 2:
                snake.getPosition().setPosY(snake.getPosition().getPosY() + 1);
                break;
            case 3:
                snake.getPosition().setPosX(snake.getPosition().getPosX() - 1);
                break;
        }

        BodyPart bp;
        if (!snake.cuerpo.isEmpty())
            for (int i = snake.cuerpo.size() - 1; i >= 0; i--) {
                bp = snake.cuerpo.get(i);
                if (i == 0) {
                    bp.setPosition(snake.getPosition());
                    switch (snake.getOrienteton()) {
                        case 0:
                            bp.getPosition().setPosY(snake.getPosition().getPosY() + 1);
                            break;
                        case 1:
                            bp.getPosition().setPosX(snake.getPosition().getPosX() - 1);
                            break;
                        case 2:
                            bp.getPosition().setPosY(snake.getPosition().getPosY() - 1);
                            break;
                        case 3:
                            bp.getPosition().setPosX(snake.getPosition().getPosX() + 1);
                            break;
                    }
                } else bp.setPosition(snake.cuerpo.get(i - 1).position);
            }
    }

    private void isScreenBorder() {
        if (snake.getPosition().getPosX() < NUM_BLOCKS_WIDE) {
            if (snake.getPosition().getPosY() < numBlocksHigh) {
                if (snake.getPosition().getPosX() < 0) {
                    snake.setPosition(new Position(NUM_BLOCKS_WIDE, snake.getPosition().getPosY()));
                } else if (snake.getPosition().getPosY() < 0)
                    snake.setPosition(new Position(snake.getPosition().PosX, numBlocksHigh));
            } else snake.setPosition(new Position(snake.getPosition().PosX, 0));
        } else snake.setPosition(new Position(0, snake.getPosition().getPosY()));

    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 10, 60, 100));
            bodyPartPaint();
            snakeHeadPaint();
            spawnApple();
            puntuacion();
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void snakeHeadPaint() {
        Bitmap snakeHead = BitmapFactory.decodeResource(context.getResources(), drawable.snake_head);
        Bitmap snakeHeadSized = Bitmap.createScaledBitmap(snakeHead, blockSize * 2, blockSize * 2, false);
        if (snake.getOrienteton() != 2) {
            if (snake.getOrienteton() != 3) {
                if (snake.getOrienteton() == 0) {
                    canvas.drawBitmap(Utils.rotateBitmap(snakeHeadSized, 180), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
                } else if (snake.getOrienteton() == 1)
                    canvas.drawBitmap(Utils.rotateBitmap(snakeHeadSized, -90), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
            } else
                canvas.drawBitmap(Utils.rotateBitmap(snakeHeadSized, 90), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
        } else
            canvas.drawBitmap(Utils.rotateBitmap(snakeHeadSized, 0), (snake.getPosition().getPosX() * blockSize) - (blockSize / 2), (snake.getPosition().getPosY() * blockSize) - (blockSize / 2), paint);
    }

    private void bodyPartPaint() {
        int i = 0;
        for (BodyPart snake1 : snake.cuerpo) {
            if (i % 2 != 0) paint.setColor(Color.argb(255, 118, 140, 48));
            else paint.setColor(Color.argb(255, 80, 110, 48));
            canvas.drawRect(snake1.getPosition().getPosX() * blockSize, (snake1.getPosition().getPosY() * blockSize), (snake1.getPosition().getPosX() * blockSize) + blockSize, (snake1.getPosition().getPosY() * blockSize) + blockSize, paint);
            i++;
        }
    }

    private void puntuacion() {
        //Puntuacion actual
        Paint textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        if (size.x <= 1000) textPaint.setTextSize(40);
        else textPaint.setTextSize(80);
        textPaint.setFakeBoldText(true);
        canvas.drawText(score + snake.getSize(),
                screenX / 2,
                2 * blockSize + blockSize / 6,
                textPaint);

        //Máxima puntuacion
        Paint textPaintMax = new Paint();
        textPaintMax.setTextAlign(Paint.Align.CENTER);
        textPaintMax.setColor(Color.WHITE);
        if (size.x <= 1000) textPaintMax.setTextSize(30);
        else textPaintMax.setTextSize(60);
        textPaintMax.setFakeBoldText(true);
        canvas.drawText(maxscore + prefManager.getMaxPoint(),
                screenX / 2,
                4 * blockSize - blockSize / 8,
                textPaintMax);
    }

    float firstX_point, firstY_point;

    @SuppressLint("ClickableViewAccessibility")
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

                if (!onpause) {
                    if (!dead) {
                        if (firstX_point != finalX || firstY_point != finalY) {

                            if (Math.abs(distanceX) <= Math.abs(distanceY)) {
                                if ((!(firstY_point < finalY))) {
                                    if (snake.cuerpo.isEmpty()) {
                                        setOirentetion(0);
                                        break;
                                    } else if (snake.getOrienteton() != 2)
                                        setOirentetion(0);
                                    break;
                                } else {
                                    if (snake.cuerpo.isEmpty()) {
                                        setOirentetion(2);
                                        break;
                                    } else if (snake.getOrienteton() != 0)
                                        setOirentetion(2);
                                    break;
                                }
                            } else {
                                if ((!(firstX_point < finalX))) {
                                    if (!snake.cuerpo.isEmpty()) {
                                        if (snake.getOrienteton() != 1)
                                            setOirentetion(3);
                                        break;
                                    } else
                                        setOirentetion(3);
                                    break;
                                } else {
                                    if (!snake.cuerpo.isEmpty()) {
                                        if (snake.getOrienteton() != 3)
                                            setOirentetion(1);
                                        break;
                                    } else
                                        setOirentetion(1);
                                    break;
                                }
                            }
                        } else {
                            if (snake.getOrienteton() != 0) {
                                if (snake.getOrienteton() != 1) {
                                    if (snake.getOrienteton() != 2) {
                                        setOirentetion(2);
                                        break;
                                    } else
                                        setOirentetion(1);
                                    break;
                                } else
                                    setOirentetion(0);
                                break;
                            } else
                                setOirentetion(3);
                            break;
                        }
                    } else {
                        dead = false;
                        new Utils().vibrateButtonApple(context);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        newGame();

                    }
                } else
                    new Utils().vibrateButtonApple(context);
                    onpause = false;
                break;
        }
        return true;
    }

}
