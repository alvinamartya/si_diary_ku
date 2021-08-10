package id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.change_password;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.BottomSheetDialogChangePasswordBinding;
import id.ac.astra.polman.sidiaryku.model.ChangePasswordModel;
import id.ac.astra.polman.sidiaryku.utils.PopupMessageHelper;

public class ChangePasswordBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetDialogChangePasswordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetDialogChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(new ViewModelProvider(this).get(ChangePasswordViewModel.class));

        binding.cancelChangePasswordButton.setOnClickListener(v -> {
            dismiss();
        });

        binding.saveChangePasswordButton.setOnClickListener(v -> {
            String title = getString(R.string.change_name_and_note);

            // show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle(title);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();

            // change password
            String oldPassword = Objects.requireNonNull(binding.oldPasswordChangePasswordText.getText()).toString();
            String newPassword = Objects.requireNonNull(binding.newPasswordChangePasswordText.getText()).toString();
            String repeatPassword = Objects.requireNonNull(binding.repeatPasswordChangePasswordText.getText()).toString();
            ChangePasswordModel changePasswordModel = new ChangePasswordModel(oldPassword, newPassword, repeatPassword);
            binding
                    .getViewModel()
                    .changePassword(this.getContext(), changePasswordModel)
                    .observe(this, responseModel -> {
                        progressDialog.dismiss();

                        if (responseModel.isSuccess()) {
                            dismiss();
                        }

                        PopupMessageHelper.show(this.getContext(), title, responseModel.getMessage());
                    });
        });
    }
}