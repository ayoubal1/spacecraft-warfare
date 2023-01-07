package com.example.spacecraftwarfare;

import static com.example.spacecraftwarfare.oyungorunumu.screenRatioX;
import static com.example.spacecraftwarfare.oyungorunumu.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Flight {
    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap ucak1, ucak2, shots, shots1, shots2, shots3, dead;
    private oyungorunumu oyungorunumu;

    Flight(oyungorunumu oyungorunumu, int screenY, Resources res) {

        this.oyungorunumu = oyungorunumu;



        ucak1 = BitmapFactory.decodeResource(res, R.drawable.ucak1);
        ucak2 = BitmapFactory.decodeResource(res, R.drawable.ucak2);

        width = ucak1.getWidth();
        height = ucak1.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        ucak1 = Bitmap.createScaledBitmap(ucak1, width, height, false);
        ucak2 = Bitmap.createScaledBitmap(ucak2, width, height, false);

        shots = BitmapFactory.decodeResource(res, R.drawable.shots);
        shots1 = BitmapFactory.decodeResource(res, R.drawable.shots1);
        shots2 = BitmapFactory.decodeResource(res, R.drawable.shots2);
        shots3 = BitmapFactory.decodeResource(res, R.drawable.shots3);


        shots = Bitmap.createScaledBitmap(shots, width, height, false);
        shots1 = Bitmap.createScaledBitmap(shots1, width, height, false);
        shots2 = Bitmap.createScaledBitmap(shots2, width, height, false);
        shots3 = Bitmap.createScaledBitmap(shots3, width, height, false);


        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }

    Bitmap getFlight () {

        if (toShoot != 0) {

            if (shootCounter == 1) {
                shootCounter++;
                return shots;
            }

            if (shootCounter == 2) {
                shootCounter++;
                return shots1;
            }

            if (shootCounter == 3) {
                shootCounter++;
                return shots2;
            }



            shootCounter = 1;
            toShoot--;
            oyungorunumu.newBullet();

            return shots3;
        }

        if (wingCounter == 0) {
            wingCounter++;
            return ucak1;
        }
        wingCounter--;

        return ucak2;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead () {
        return dead;
    }
    }
