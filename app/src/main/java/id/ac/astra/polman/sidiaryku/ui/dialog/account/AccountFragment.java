package id.ac.astra.polman.sidiaryku.ui.dialog.account;

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
import id.ac.astra.polman.sidiaryku.ui.fragment.profile.ProfileViewModel;
import id.ac.astra.polman.sidiaryku.utils.PopupMessage;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class AccountFragment extends BottomSheetDialogFragment {

    private TextInputEditText editTextName, editTextNote;
    private Button saveBtn, cancelBtn;
    private final ProfileViewModel profileViewModel;

    public AccountFragment(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_fragment, container, false);

        editTextName = v.findViewById(R.id.name_account_edit_text);
        editTextNote = v.findViewById(R.id.note_account_edit_text);
        saveBtn = v.findViewById(R.id.save_account_button);
        cancelBtn = v.findViewById(R.id.cancel_account_button);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AccountViewModel viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        Preference preference = new Preference(this.requireContext());
        UserEntity userEntity = preference.getUser();

        // binding old data to all fields
        editTextName.setText(userEntity.getName());
        editTextNote.setText(userEntity.getNote());

        // cancel any changes
        cancelBtn.setOnClickListener(v -> {
            closeDialog();
        });

        // save changes to fire store
        saveBtn.setOnClickListener(v -> {
            String title = getString(R.string.change_name_and_note);

            // show progress dialog
            ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle(title);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();

            // change name and note
            String name = Objects.requireNonNull(editTextName.getText()).toString();
            String note = Objects.requireNonNull(editTextNote.getText()).toString();
            AccountModel accountModel = new AccountModel(name, note);
            viewModel
                    .changeNameAndNote(this.getContext(), accountModel)
                    .observe(this, responseModel -> {
                        progressDialog.dismiss();

                        if (responseModel.isSuccess()) {
                            // change data in preference
                            userEntity.setNote(note);
                            userEntity.setName(name);
                            preference.setUser(userEntity);

                            // dismiss
                            closeDialog();
                        } else {
                            PopupMessage.show(this.getContext(), title, responseModel.getMessage());
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