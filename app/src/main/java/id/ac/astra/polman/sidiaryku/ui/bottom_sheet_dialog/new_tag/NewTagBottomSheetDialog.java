package id.ac.astra.polman.sidiaryku.ui.bottom_sheet_dialog.new_tag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import id.ac.astra.polman.sidiaryku.databinding.BottomSheetDialogNewTagBinding;

public class NewTagBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetDialogNewTagBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetDialogNewTagBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(new ViewModelProvider(this).get(NewTagViewModel.class));

        binding.cancelNewTagButton.setOnClickListener(v -> {
            dismiss();
        });
    }
}