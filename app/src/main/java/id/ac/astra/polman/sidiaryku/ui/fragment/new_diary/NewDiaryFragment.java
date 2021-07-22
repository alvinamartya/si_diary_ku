package id.ac.astra.polman.sidiaryku.ui.fragment.new_diary;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import id.ac.astra.polman.sidiaryku.databinding.FragmentNewDiaryBinding;
import id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.new_tag.NewTagBottomSheetDialog;

public class NewDiaryFragment extends Fragment {
    private final static String TAG = NewDiaryFragment.class.getSimpleName();
    private FragmentNewDiaryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewDiaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(new ViewModelProvider(this).get(NewDiaryViewModel.class));

        binding.backNewDiaryImage.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        // count length of character left diary
        binding.getViewModel().getLengthCharacterLeftLiveData().observe(this.getViewLifecycleOwner(), lengthCharacter -> {
            Log.e(TAG, "onViewCreated: " + lengthCharacter);
            binding.lengthCharacterNewDiaryText.setText(String.valueOf(lengthCharacter));
        });

        binding.typeWriteNewDiaryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.getViewModel().setLengthCharacterLeftLiveData(Objects.requireNonNull(binding.typeWriteNewDiaryText.getText()).toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        
        binding.addTagsNewDiaryButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = this.getChildFragmentManager();
            NewTagBottomSheetDialog newTagBottomSheetDialog = new NewTagBottomSheetDialog();
            newTagBottomSheetDialog.show(fragmentManager, TAG);
        });

    }
}