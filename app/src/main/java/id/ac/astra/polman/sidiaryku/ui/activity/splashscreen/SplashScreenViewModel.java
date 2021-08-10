package id.ac.astra.polman.sidiaryku.ui.activity.splashscreen;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;

import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.LoginModel;
import id.ac.astra.polman.sidiaryku.ui.activity.main.MainActivity;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginActivity;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginViewModel;
import id.ac.astra.polman.sidiaryku.utils.MoveViewHelper;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class SplashScreenViewModel {
    private final static String TAG = SplashScreenViewModel.class.getSimpleName();

    public void checkLogin(Activity activity) {
        LoginViewModel loginViewModel = new LoginViewModel();

        PreferenceHelper preferenceHelper = new PreferenceHelper(activity);
        UserEntity userEntity = preferenceHelper.getUser();

        // move to next screen
        if (userEntity != null) {
            LoginModel loginModel = new LoginModel(userEntity.getEmail(), userEntity.getPassword());

            // login and move to home page
            loginViewModel
                    .login(activity, loginModel)
                    .observe((LifecycleOwner) activity, responseModel -> {
                        if (responseModel.isSuccess()) {
                            MoveViewHelper.withFinish(activity, MainActivity.class);
                        } else {
                            MoveViewHelper.withFinish(activity, LoginActivity.class);
                        }
                    });
        } else {
            // move to login with delay 5 sec
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        MoveViewHelper.withFinish(activity, LoginActivity.class);
                    },
                    5000);
        }
    }
}
