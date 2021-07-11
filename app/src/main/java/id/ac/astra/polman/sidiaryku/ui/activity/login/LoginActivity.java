package id.ac.astra.polman.sidiaryku.ui.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.ActivityLoginBinding;
import id.ac.astra.polman.sidiaryku.ui.activity.signup.SignUpActivity;
import id.ac.astra.polman.sidiaryku.utils.MoveView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding activity
        ActivityLoginBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_login);

        // move to sign up view
        binding.signUpLoginButton.setOnClickListener(v -> {
            MoveView.withoutFinish(this, SignUpActivity.class);
        });
    }
}