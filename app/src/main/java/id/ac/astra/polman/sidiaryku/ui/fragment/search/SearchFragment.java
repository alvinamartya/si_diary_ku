package id.ac.astra.polman.sidiaryku.ui.fragment.search;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.databinding.FragmentMemoryBinding;
import id.ac.astra.polman.sidiaryku.databinding.FragmentSearchBinding;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;
import id.ac.astra.polman.sidiaryku.ui.adapter.DiaryAdapter;
import id.ac.astra.polman.sidiaryku.ui.fragment.memory.MemoryViewModel;
import id.ac.astra.polman.sidiaryku.utils.DateHelper;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(new ViewModelProvider(this).get(SearchViewModel.class));
        binding.backSearchImage.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.getViewModel().getDiaryLiveData().observe(this.getViewLifecycleOwner(), diaryModelList -> {
            List<DiaryEntity> tempDiaryList = diaryModelList;
            String search = binding.searchMemoryText.getText().toString().toLowerCase();
            if (!search.equals("")) {
                tempDiaryList = tempDiaryList
                        .stream()
                        .filter(x -> x.getAddress().toLowerCase().contains(search) ||
                                x.getDate().toLowerCase().contains(search) ||
                                x.getDiary().toLowerCase().contains(search) ||
                                x.getTagList().stream().anyMatch(z -> z.toLowerCase().contains(search)))
                        .sorted(Comparator
                                .comparing(DiaryEntity::getDate).reversed())
                        .collect(Collectors.toList());
            }

            layoutCondition(!tempDiaryList.isEmpty());
            binding.listMemoryRv.setAdapter(new DiaryAdapter(this.requireContext(), tempDiaryList));
        });
        binding.searchMemoryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshDiaryModel();
            }
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
}