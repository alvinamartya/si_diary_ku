package id.ac.astra.polman.sidiaryku.ui.fragment.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import id.ac.astra.polman.sidiaryku.dao.DiaryDao;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;

public class SearchViewModel extends ViewModel {
    private final static String TAG = SearchViewModel.class.getSimpleName();
    public LiveData<List<DiaryEntity>> getDiaryLiveData() {
        DiaryDao.initialize();
        return DiaryDao.getDiaryLiveData();
    }
}