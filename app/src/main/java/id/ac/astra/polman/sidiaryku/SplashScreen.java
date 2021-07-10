package id.ac.astra.polman.sidiaryku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import id.ac.astra.polman.sidiaryku.utils.MoveView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // delay 2 sec
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    MoveView.withFinish(SplashScreen.this, LoginActivity.class);
                },
                2000);
    }
}