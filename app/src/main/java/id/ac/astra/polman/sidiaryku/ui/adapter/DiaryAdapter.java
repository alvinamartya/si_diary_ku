package id.ac.astra.polman.sidiaryku.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import id.ac.astra.polman.sidiaryku.model.DiaryModel;
import id.ac.astra.polman.sidiaryku.ui.fragment.memory.MemoryViewModel;
import id.ac.astra.polman.sidiaryku.ui.holder.DiaryHolder;
import id.ac.astra.polman.sidiaryku.ui.holder.ProgressDiaryHolder;

public class DiaryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<DiaryModel> diaryModelList;
    private MemoryViewModel memoryViewModel;
    private final static int full_key = 100;
    private final static int progress_key = 101;

    public DiaryAdapter(Context context, List<DiaryModel> diaryModelList, MemoryViewModel memoryViewModel) {
        this.context = context;
        this.diaryModelList = diaryModelList;
        this.memoryViewModel = memoryViewModel;
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
        DiaryModel diaryModel = diaryModelList.get(position);
        if (diaryModel.getProgress() < 100)
            ((ProgressDiaryHolder) holder).bind(diaryModel);
        else
            ((DiaryHolder) holder).bind(context, diaryModel, memoryViewModel);
    }

    @Override
    public int getItemCount() {
        return diaryModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        DiaryModel diaryModel = diaryModelList.get(position);
        if (diaryModel.getProgress() < 100) return progress_key;
        else return full_key;
    }
}
