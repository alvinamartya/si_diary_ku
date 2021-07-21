package id.ac.astra.polman.sidiaryku.ui.fragment.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.FragmentProfileBinding;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginActivity;
import id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.account.AccountFragment;
import id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.change_password.ChangePasswordFragment;
import id.ac.astra.polman.sidiaryku.utils.MoveView;
import id.ac.astra.polman.sidiaryku.utils.PopupMessage;

public class ProfileFragment extends Fragment {

    private final static String TAG = ProfileFragment.class.getSimpleName();
    private ProfileViewModel viewModel;
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.loadUser(this.getContext());
        viewModel.getUser().observe(this.getViewLifecycleOwner(), userEntity -> {
            String note = userEntity.getNote().equals("") ? getString(R.string.we_hope_you_always_happy) : userEntity.getNote();
            binding.nameProfileText.setText(userEntity.getName());
            binding.noteProfileText.setText(note);
        });

        // account layout is clicked
        binding.accountProfileLayout.setOnClickListener(v -> {
            FragmentManager fragmentManager = this.getChildFragmentManager();
            AccountFragment accountFragment = new AccountFragment(viewModel);
            accountFragment.show(fragmentManager, TAG);
        });

        // change layout is clicked
        binding.changePasswordProfileLayout.setOnClickListener(v -> {
            FragmentManager fragmentManager = this.getChildFragmentManager();
            ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
            changePasswordFragment.show(fragmentManager, TAG);
        });

        // logout layout is clicked
        binding.logoutProfileLayout.setOnClickListener(v -> PopupMessage.confirm(
                getContext(),
                getString(R.string.logout),
                getString(R.string.are_you_sure),
                (successDialog, which) -> {
                    viewModel.logout(getActivity());
                    MoveView.withFinish(getContext(), LoginActivity.class);
                },
                (failedDialog, which) -> {
                    // do nothing
                }));
    }
}