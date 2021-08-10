package id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.account;

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
import id.ac.astra.polman.sidiaryku.databinding.BottomSheetDialogAccountBinding;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.model.AccountModel;
import id.ac.astra.polman.sidiaryku.ui.fragment.profile.ProfileViewModel;
import id.ac.astra.polman.sidiaryku.utils.PopupMessageHelper;
import id.ac.astra.polman.sidiaryku.utils.PreferenceHelper;

public class AccountBottomSheetDialog extends BottomSheetDialogFragment {

    private final ProfileViewModel profileViewModel;
    private BottomSheetDialogAccountBinding binding;

    public AccountBottomSheetDialog(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetDialogAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PreferenceHelper preferenceHelper = new PreferenceHelper(this.requireContext());
        UserEntity userEntity = preferenceHelper.getUser();
        binding.setViewModel(new ViewModelProvider(this).get(AccountViewModel.class));

        // binding old data to all fields
        binding.nameAccountEditText.setText(userEntity.getName());
        binding.noteAccountEditText.setText(userEntity.getNote());

        // cancel any changes
        binding.cancelAccountButton.setOnClickListener(v -> {
            closeDialog();
        });

        // save changes to fire store
        binding.saveAccountButton.setOnClickListener(v -> {
            String title = getString(R.string.change_name_and_note);

            // show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle(title);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();

            // change name and note
            String name = Objects.requireNonNull(binding.nameAccountEditText.getText()).toString();
            String note = Objects.requireNonNull(binding.noteAccountEditText.getText()).toString();
            AccountModel accountModel = new AccountModel(name, note);
            binding
                    .getViewModel()
                    .changeNameAndNote(this.getContext(), accountModel)
                    .observe(this, responseModel -> {
                        progressDialog.dismiss();

                        if (responseModel.isSuccess()) {
                            // change data in preference
                            userEntity.setNote(note);
                            userEntity.setName(name);
                            preferenceHelper.setUser(userEntity);

                            // dismiss
                            closeDialog();
                        } else {
                            PopupMessageHelper.show(this.getContext(), title, responseModel.getMessage());
                        }
                    });
        });
    }

    // load any changes and dismiss dialog
    private void closeDialog() {
        profileViewModel.loadUser(this.getContext());
        dismiss();
    }
}