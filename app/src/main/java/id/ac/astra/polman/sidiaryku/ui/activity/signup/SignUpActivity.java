package id.ac.astra.polman.sidiaryku.ui.activity.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.ActivitySignUpBinding;
import id.ac.astra.polman.sidiaryku.model.SignUpModel;
import id.ac.astra.polman.sidiaryku.ui.activity.MainActivity;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginActivity;
import id.ac.astra.polman.sidiaryku.utils.MoveView;
import id.ac.astra.polman.sidiaryku.utils.PopupMessage;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // binding activity
        ActivitySignUpBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_sign_up);

        binding.setViewModel(new SignUpViewModel());

        // move to login
        binding.loginSignUpButton.setOnClickListener(v -> {
            MoveView.withoutFinish(this, LoginActivity.class);
        });

        // sign up
        binding.signUpButton.setOnClickListener(v -> {
            String title = getString(R.string.sign_up);

            // show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(title);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();

            // get data in ui
            String email = Objects.requireNonNull(binding.emailSignUpText.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordSignUpText.getText()).toString();
            String repeatPassword = Objects.requireNonNull(binding.repeatPasswordSignUpText.getText()).toString();
            String name = Objects.requireNonNull(binding.nameText.getText()).toString();
            SignUpModel signUpModel = new SignUpModel(email, name, password, repeatPassword);

            // sign up
            binding
                    .getViewModel()
                    .signUp(this, signUpModel)
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