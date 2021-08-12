package id.ac.astra.polman.sidiaryku.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import id.ac.astra.polman.sidiaryku.entity.DiaryEntity;
import id.ac.astra.polman.sidiaryku.ui.fragment.memory.MemoryViewModel;
import id.ac.astra.polman.sidiaryku.ui.fragment.search.SearchViewModel;
import id.ac.astra.polman.sidiaryku.ui.holder.DiaryHolder;
import id.ac.astra.polman.sidiaryku.ui.holder.ProgressDiaryHolder;

public class DiaryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<DiaryEntity> diaryEntityList;
    private final static int full_key = 100;
    private final static int progress_key = 101;

    public DiaryAdapter(Context context, List<DiaryEntity> diaryEntityList) {
        this.context = context;
        this.diaryEntityList = diaryEntityList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == full_key) {
            return new DiaryHolder(inflater, parent);
        } else {
            return new ProgressDiaryHolder(inflater, parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        DiaryEntity diaryEntity = diaryEntityList.get(position);
        if (diaryEntity.getProgress() < 100)
            ((ProgressDiaryHolder) holder).bind(diaryEntity);
        else
            ((DiaryHolder) holder).bind(context, diaryEntity);
    }

    @Override
    public int getItemCount() {
        return diaryEntityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        DiaryEntity diaryEntity = diaryEntityList.get(position);
        if (diaryEntity.getProgress() < 100) return progress_key;
        else return full_key;
    }
}
