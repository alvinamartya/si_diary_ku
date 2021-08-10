package id.ac.astra.polman.sidiaryku.ui.holder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import id.ac.astra.polman.sidiaryku.R;
import id.ac.astra.polman.sidiaryku.dao.TagDao;

public class TagHolder extends RecyclerView.ViewHolder {
    private final TextView tagText;

    public TagHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.tag_layout, parent, false));
        tagText = itemView.findViewById(R.id.tag_text);
    }

    @SuppressLint("SetTextI18n")
    public void bind(Context context, String title, String tag) {
        tagText.setText("#" + tag);
        tagText.setOnClickListener(view -> {
            CharSequence[] items = {
                    context.getString(R.string.delete_tag),
                    context.getString(R.string.cancel)
            };

            new AlertDialog
                    .Builder(context)
                    .setTitle(title)
                    .setItems(items, (dialog, i) -> {
                        if (items[i].toString().equals(context.getString(R.string.delete_tag))) {
                            TagDao.initialize();
                            TagDao.removeTagLiveData(tagText.getText().toString());
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    })
                    .show();
        });
    }
}
