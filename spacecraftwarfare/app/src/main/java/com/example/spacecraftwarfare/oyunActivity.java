package com.example.spacecraftwarfare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class oyunActivity extends AppCompatActivity {
    private oyungorunumu yungorunumu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);


        yungorunumu = new oyungorunumu(this, point.x, point.y);

        setContentView(yungorunumu);

    }

    @Override
    protected void onPause() {
        super.onPause();
        yungorunumu.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        yungorunumu.resume();
    }
}
