package id.ac.astra.polman.sidiaryku.ui.fragment.profile;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.FragmentProfileBinding;
import id.ac.astra.polman.sidiaryku.entity.UserEntity;
import id.ac.astra.polman.sidiaryku.ui.activity.login.LoginActivity;
import id.ac.astra.polman.sidiaryku.utils.MoveView;
import id.ac.astra.polman.sidiaryku.utils.PopupMessage;
import id.ac.astra.polman.sidiaryku.utils.Preference;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private FragmentProfileBinding binding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

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

        Preference preference = new Preference(requireContext());
        UserEntity user = preference.getUser();

        String hay = getResources().getString(R.string.profile_hay);
        String hope = getResources().getString(R.string.profile_hope);
        binding.profileText.setText(hay + " " + user.getName() + ", " + hope);

        binding.logoutProfileButton.setOnClickListener(v -> {
            PopupMessage.confirm(
                    getContext(),
                    "Logout",
                    "Are you sure?",
                    (successDialog, which) -> {
                        viewModel.logout(getActivity());
                        MoveView.withFinish(getContext(), LoginActivity.class);
                    },
                    (failedDialog, which) -> {
                        // do nothing
                    });
        });
    }
}