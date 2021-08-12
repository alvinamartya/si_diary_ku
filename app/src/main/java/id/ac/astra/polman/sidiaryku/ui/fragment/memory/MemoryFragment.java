package id.ac.astra.polman.sidiaryku.ui.fragment.memory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.databinding.FragmentMemoryBinding;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;
import id.ac.astra.polman.sidiaryku.ui.adapter.DiaryAdapter;
import id.ac.astra.polman.sidiaryku.utils.DateHelper;

public class MemoryFragment extends Fragment {
    private final static String TAG = MemoryFragment.class.getSimpleName();
    private FragmentMemoryBinding binding;
    private boolean isAllTimeMemory = true;

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
        binding.searchMemoryImage.setOnClickListener(v-> {
            Navigation
                    .findNavController(v)
                    .navigate(R.id.action_navigation_memory_to_searchFragment);
        });
        binding.allMemoriesButton.setOnClickListener(v -> {
            setEnabledAllMemoriesButton(false);
            refreshDiaryModel();
        });
        binding.onThisDayMemoriesButton.setOnClickListener(v -> {
            setEnabledAllMemoriesButton(true);
            refreshDiaryModel();
        });
        binding.getViewModel().getDiaryLiveData().observe(this.getViewLifecycleOwner(), diaryModelList -> {
            List<DiaryEntity> tempDiaryList = new ArrayList<>();
            if (!isAllTimeMemory) {
                List<DiaryEntity> onThisDiaryDaoList = diaryModelList
                        .stream()
                        .filter(x -> x.getDate().contains(DateHelper.getCurrentDate("dd MMM yyyy")))
                        .collect(Collectors.toList());
                tempDiaryList.addAll(onThisDiaryDaoList);
            } else {
                tempDiaryList.addAll(diaryModelList);
            }

            layoutCondition(!tempDiaryList.isEmpty());
            binding.listMemoryRv.setAdapter(new DiaryAdapter(this.requireContext(), tempDiaryList));
        });
    }

    private void layoutCondition(boolean isNotEmpty) {
        if (isNotEmpty) {
            binding.listMemoryRv.setVisibility(View.VISIBLE);
            binding.emptyMemoryLayout.setVisibility(View.GONE);
        } else {
            binding.listMemoryRv.setVisibility(View.GONE);
            binding.emptyMemoryLayout.setVisibility(View.VISIBLE);
        }
    }

    private void refreshDiaryModel() {
        DiaryDao.initialize();
        DiaryDao.refreshDiaryLiveData();
    }

    private void setEnabledAllMemoriesButton(boolean value) {
        isAllTimeMemory = !value;
        binding.allMemoriesButton.setEnabled(value);
        binding.onThisDayMemoriesButton.setEnabled(!value);
    }
}