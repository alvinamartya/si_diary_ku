package id.ac.astra.polman.sidiaryku.ui.fragment.memory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import id.ac.astra.polman.sidiaryku.databinding.FragmentMemoryBinding;
import id.ac.astra.polman.sidiaryku.model.DiaryModel;
import id.ac.astra.polman.sidiaryku.ui.adapter.DiaryAdapter;
import id.ac.astra.polman.sidiaryku.ui.adapter.TagAdapter;
import id.ac.astra.polman.sidiaryku.ui.fragment.new_diary.NewDiaryViewModel;

public class MemoryFragment extends Fragment {
    private final static String TAG = MemoryFragment.class.getSimpleName();
    private FragmentMemoryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMemoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(new ViewModelProvider(this).get(MemoryViewModel.class));
        binding.allMemoriesButton.setOnClickListener(v -> binding.getViewModel().viewAllTimeDiary());
        binding.onThisDayMemoriesButton.setOnClickListener(v -> binding.getViewModel().viewOnThisDayDiary());
        binding.getViewModel().getDiaryLiveData().observe(this.getViewLifecycleOwner(), diaryModelList -> {
            layoutCondition(!diaryModelList.isEmpty());
            binding.listMemoryRv.setAdapter(new DiaryAdapter(this.requireContext(), diaryModelList));
        });
    }

    private void layoutCondition(boolean isNotEmpty) {
        if(isNotEmpty) {
            binding.listMemoryRv.setVisibility(View.VISIBLE);
            binding.emptyMemoryLayout.setVisibility(View.GONE);
        } else {
            binding.listMemoryRv.setVisibility(View.GONE);
            binding.emptyMemoryLayout.setVisibility(View.VISIBLE);
        }
    }
}