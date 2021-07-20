package id.ac.astra.polman.sidiaryku.ui.dialog.account;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.ui.fragment.profile.ProfileViewModel;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class AccountFragment extends DialogFragment {

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

        editTextName.setText(userEntity.getName());
        editTextNote.setText(userEntity.getNote());

        cancelBtn.setOnClickListener(v -> {
            profileViewModel.loadUser(this.getContext());
            dismiss();
        });

        saveBtn.setOnClickListener(v -> {

        });
    }
}