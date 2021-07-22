package id.ac.astra.polman.sidiaryku.ui.activity.splashscreen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding
        ActivitySplashScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        binding.setViewModel(new SplashScreenViewModel());
        binding.getViewModel().checkLogin(this);
    }
}