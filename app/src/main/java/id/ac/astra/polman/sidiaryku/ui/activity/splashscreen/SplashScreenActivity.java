package id.ac.astra.polman.sidiaryku.ui.activity.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginActivity;
import id.ac.astra.polman.sidiaryku.utils.MoveView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // delay 2 sec
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    MoveView.withFinish(SplashScreenActivity.this, LoginActivity.class);
                },
                2000);
    }
}