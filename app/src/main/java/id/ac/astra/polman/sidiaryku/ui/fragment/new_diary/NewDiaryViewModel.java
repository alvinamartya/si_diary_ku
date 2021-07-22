package id.ac.astra.polman.sidiaryku.ui.fragment.new_diary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewDiaryViewModel extends ViewModel {
    private final int maxLengthCharacter = 200;
    private final MutableLiveData<Integer> lengthCharacterLeftLiveData = new MutableLiveData<>(maxLengthCharacter);

    public LiveData<Integer> getLengthCharacterLeftLiveData() {
        return lengthCharacterLeftLiveData;
    }

    public void setLengthCharacterLeftLiveData(int lengthCharacter) {
        lengthCharacterLeftLiveData.postValue(maxLengthCharacter - lengthCharacter);
    }
}