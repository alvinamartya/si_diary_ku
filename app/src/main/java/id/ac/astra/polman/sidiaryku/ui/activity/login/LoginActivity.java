package id.ac.astra.polman.sidiaryku.ui.activity.login;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.ActivityLoginBinding;
import id.ac.astra.polman.sidiaryku.model.LoginModel;
import id.ac.astra.polman.sidiaryku.ui.activity.MainActivity;
import id.ac.astra.polman.sidiaryku.ui.activity.signup.SignUpActivity;
import id.ac.astra.polman.sidiaryku.utils.MoveView;
import id.ac.astra.polman.sidiaryku.utils.PopupMessage;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding activity
        ActivityLoginBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_login);

        binding.setViewModel(new LoginViewModel());

        // move to sign up view
        binding.signUpLoginButton.setOnClickListener(v -> {
            MoveView.withoutFinish(this, SignUpActivity.class);
        });

        // login
        binding.loginButton.setOnClickListener(v -> {
            String title = getString(R.string.login);

            // show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(title);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();

            // binding data in ui
            String email = Objects.requireNonNull(binding.emailLoginText.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordLoginText.getText()).toString();
            LoginModel loginModel = new LoginModel(email, password);

            binding
                    .getViewModel()
                    .login(this, loginModel)
                    .observe(this, responseModel -> {
                        progressDialog.dismiss();

                        if (responseModel.isSuccess()) {
                            MoveView.withFinish(this, MainActivity.class);
                        } else {
                            PopupMessage.show(this, title, responseModel.getMessage());
                        }
                    });
        });
    }
}