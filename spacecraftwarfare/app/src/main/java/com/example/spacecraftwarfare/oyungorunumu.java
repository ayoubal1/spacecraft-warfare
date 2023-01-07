package com.example.spacecraftwarfare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class oyungorunumu extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY,score = 0;
    private Background background1, background2;
    private Flight Flight;
    private Paint paint;
    private Random random;
    private List<Bullet> bullets;
    private SharedPreferences prefs;
    private spacecraft[] spacecrafts;
    private oyunActivity activity;
    private int sound;
    private SoundPool soundPool;
    public static float screenRatioX, screenRatioY;


    public oyungorunumu(Context context,int screenX, int screenY) {
        super(context);
        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.fire, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;




        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.x = screenX;
        Flight = new Flight(this, screenY, getResources());
        bullets = new ArrayList<>();
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        spacecrafts = new spacecraft[4];

        for (int i = 0;i < 4;i++) {

            spacecraft spac = new spacecraft(getResources());
            spacecrafts[i] = spac;

        }

        random = new Random();


    }



    public void resume() {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void run() {

        while (isPlaying) {

            update();
            draw();
            sleep();

        }
    }
    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        background1.x -= 10 * screenRatioX ;
        background2.x -= 10 * screenRatioX ;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }
        if (Flight.isGoingUp)
            Flight.y -= 30 * screenRatioY;
        else
            Flight.y += 30 * screenRatioY;

        if (Flight.y < 0)
            Flight.y = 0;

        if (Flight.y >= screenY - Flight.height)
            Flight.y = screenY - Flight.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 50 * screenRatioX;

            for (spacecraft spac : spacecrafts) {

                if (Rect.intersects(spac.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    spac.x = -500;
                    bullet.x = screenX + 500;
                    spac.wasShot = true;

                }

            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for (spacecraft spac : spacecrafts) {

            spac.x -= spac.speed;

            if (spac.x + spac.width < 0) {

                if (!spac.wasShot) {
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                spac.speed = random.nextInt(bound);

                if (spac.speed < 10 * screenRatioX)
                    spac.speed = (int) (10 * screenRatioX);

                spac.x = screenX;
                spac.y = random.nextInt(screenY - spac.height);

                spac.wasShot = false;
            }

            if (Rect.intersects(spac.getCollisionShape(), Flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }

        }

    }







    private void draw() {
        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(Flight.getFlight(), Flight.x, Flight.y, paint);
            getHolder().unlockCanvasAndPost(canvas);
            for (spacecraft spac : spacecrafts)
                canvas.drawBitmap(spac.getBird(), spac.x, spac.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(Flight.getDead(), Flight.x, Flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(Flight.getFlight(), Flight.x, Flight.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);


        }
    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    public void pause ( ) {

            try {
                isPlaying = false;
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    public void newBullet() {
        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0, 0, 1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = Flight.x + Flight.width;
        bullet.y = Flight.y + (Flight.height / 2);
        bullets.add(bullet);


    }




   public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    Flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Flight.isGoingUp = false;
                if (event.getX() > screenX / 2)
                    Flight.toShoot++;
                break;
        }

        return true;
        
        
    }
}