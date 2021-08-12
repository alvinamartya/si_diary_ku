package id.ac.astra.polman.sidiaryku.ui.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;

public class ProgressDiaryHolder extends RecyclerView.ViewHolder {
    private final TextView dateDiaryText;
    private final ProgressBar progressDiary;

    public ProgressDiaryHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.diary_progress_layout, parent, false));
        dateDiaryText = itemView.findViewById(R.id.date_diary_text);
        progressDiary = itemView.findViewById(R.id.diary_progress);
    }

    public void bind(DiaryEntity diaryEntity) {
        dateDiaryText.setText(diaryEntity.getDate());
        progressDiary.setProgress(diaryEntity.getProgress());
    }
}
