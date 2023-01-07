package com.example.spacecraftwarfare;

import static com.example.spacecraftwarfare.oyungorunumu.screenRatioX;
import static com.example.spacecraftwarfare.oyungorunumu.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class spacecraft {
    public int speed = 20;
    public boolean wasShot = true;
    int x = 0, y, width, height, birdCounter = 1;
    Bitmap spacecraft1, spacecraft2, spacecraft3, spacecraft4;

    spacecraft (Resources res) {

        spacecraft1 = BitmapFactory.decodeResource(res, R.drawable.spacecraft1);
        spacecraft2 = BitmapFactory.decodeResource(res, R.drawable.spacecraft2);
        spacecraft3 = BitmapFactory.decodeResource(res, R.drawable.spacecraft3);
        spacecraft4 = BitmapFactory.decodeResource(res, R.drawable.spacecraft4);

        width = spacecraft1.getWidth();
        height = spacecraft1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        spacecraft1 = Bitmap.createScaledBitmap(spacecraft1, width, height, false);
        spacecraft2 = Bitmap.createScaledBitmap(spacecraft2, width, height, false);
        spacecraft3 = Bitmap.createScaledBitmap(spacecraft3, width, height, false);
        spacecraft4 = Bitmap.createScaledBitmap(spacecraft4, width, height, false);

        y = -height;
    }

    Bitmap getBird () {

        if (birdCounter == 1) {
            birdCounter++;
            return spacecraft1;
        }

        if (birdCounter == 2) {
            birdCounter++;
            return spacecraft2;
        }

        if (birdCounter == 3) {
            birdCounter++;
            return spacecraft3;
        }

        birdCounter = 1;

        return spacecraft4;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
