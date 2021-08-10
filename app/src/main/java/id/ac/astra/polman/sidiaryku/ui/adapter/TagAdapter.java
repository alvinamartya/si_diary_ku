package id.ac.astra.polman.sidiaryku.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.ui.holder.TagHolder;

public class TagAdapter extends RecyclerView.Adapter<TagHolder> {
    private Context context;
    private List<String> tagList;

    public TagAdapter(Context context, List<String> tagList) {
        this.context = context;
        this.tagList = tagList;
    }

    @NonNull
    @NotNull
    @Override
    public TagHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new TagHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TagHolder holder, int position) {
        String tag = tagList.get(position);
        String title = context.getString(R.string.tag);
        holder.bind(context, title, tag);
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }
}
