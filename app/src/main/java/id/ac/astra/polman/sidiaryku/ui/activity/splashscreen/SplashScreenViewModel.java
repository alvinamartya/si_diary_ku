package id.ac.astra.polman.sidiaryku.ui.activity.splashscreen;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.google.gson.Gson;

import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.LoginModel;
import id.ac.astra.polman.sidiaryku.ui.activity.MainActivity;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginActivity;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginViewModel;
import id.ac.astra.polman.sidiaryku.utils.FirebaseAnalyticsHelper;
import id.ac.astra.polman.sidiaryku.utils.MoveView;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class SplashScreenViewModel {
    private final static String TAG = SplashScreenViewModel.class.getSimpleName();

    public void checkLogin(Activity activity) {
        LoginViewModel loginViewModel = new LoginViewModel();

        Preference preference = new Preference(activity);
        UserEntity userEntity = preference.getUser();

        // move to next screen
        if (userEntity != null) {
            LoginModel loginModel = new LoginModel(userEntity.getEmail(), userEntity.getPassword());

            // login and move to home page
            loginViewModel
                    .login(activity, loginModel)
                    .observe((LifecycleOwner) activity, responseModel -> {
                        if (responseModel.isSuccess()) {
                            MoveView.withFinish(activity, MainActivity.class);
                        } else {
                            MoveView.withFinish(activity, LoginActivity.class);
                        }
                    });
        } else {
            // move to login with delay 5 sec
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        MoveView.withFinish(activity, LoginActivity.class);
                    },
                    5000);
        }
    }
}
