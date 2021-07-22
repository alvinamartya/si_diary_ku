package id.ac.astra.polman.sidiaryku.ui.fragment.diary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.databinding.FragmentDiaryBinding;

public class DiaryFragment extends Fragment {
    private FragmentDiaryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.writeButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_diary_to_newDiaryFragment));
    }
}