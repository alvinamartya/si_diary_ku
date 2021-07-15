package id.ac.astra.polman.sidiaryku.ui.activity.splashscreen;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;

import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.LoginModel;
import id.ac.astra.polman.sidiaryku.ui.activity.MainActivity;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginActivity;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginViewModel;
import id.ac.astra.polman.sidiaryku.utils.MoveView;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class SplashScreenViewModel {
    public void checkLogin(Activity activity) {
        LoginViewModel loginViewModel = new LoginViewModel();
        Context context = (Context)activity;

        Preference preference = new Preference(context);
        UserEntity userEntity = preference.getUser();

        if (userEntity != null) {
            // login and move to home page
            loginViewModel
                    .login(activity, new LoginModel())
                    .observe((LifecycleOwner) activity, responseModel -> {
                        if (responseModel.isSuccess()) {
                            MoveView.withFinish(context, MainActivity.class);
                        } else {
                            MoveView.withFinish(context, LoginActivity.class);
                        }
                    });
        } else {
            // move to login with delay 5 sec
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        MoveView.withFinish(context, LoginActivity.class);
                    },
                    5000);
        }
    }
}
