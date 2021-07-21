package id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.change_password;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.AccountModel;
import id.ac.astra.polman.sidiaryku.model.ChangePasswordModel;
import id.ac.astra.polman.sidiaryku.utils.PopupMessage;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class ChangePasswordFragment extends BottomSheetDialogFragment {

    private ChangePasswordViewModel viewModel;
    private TextInputEditText editTextOldPassword, editTextNewPassword, editTextRepeatPassword;
    private Button saveBtn, cancelBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);

        editTextNewPassword = v.findViewById(R.id.new_password_change_password_text);
        editTextOldPassword = v.findViewById(R.id.old_password_change_password_text);
        editTextRepeatPassword = v.findViewById(R.id.repeat_password_change_password_text);
        saveBtn = v.findViewById(R.id.save_change_password_button);
        cancelBtn = v.findViewById(R.id.cancel_change_password_button);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChangePasswordViewModel viewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);

        Preference preference = new Preference(this.requireContext());
        UserEntity userEntity = preference.getUser();

        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });

        saveBtn.setOnClickListener(v -> {
            String title = getString(R.string.change_name_and_note);

            // show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle(title);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();

            // change password
            String oldPassword = Objects.requireNonNull(editTextOldPassword.getText()).toString();
            String newPassword = Objects.requireNonNull(editTextNewPassword.getText()).toString();
            String repeatPassword = Objects.requireNonNull(editTextRepeatPassword.getText()).toString();
            ChangePasswordModel changePasswordModel = new ChangePasswordModel(oldPassword, newPassword, repeatPassword);
            viewModel
                    .changePassword(this.getContext(), changePasswordModel)
                    .observe(this, responseModel -> {
                        progressDialog.dismiss();

                        if (responseModel.isSuccess()) {
                            dismiss();
                        }

                        PopupMessage.show(this.getContext(), title, responseModel.getMessage());
                    });
        });
    }
}