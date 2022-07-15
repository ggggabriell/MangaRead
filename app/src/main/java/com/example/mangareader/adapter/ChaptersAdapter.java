package com.example.mangareader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangareader.R;
import com.example.mangareader.domain.model.ChapterModel;
import com.example.mangareader.domain.model.MangaModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.ViewHolder> {

    private List<ChapterModel> list = new ArrayList<>();
    private final OnClick onClick;

    public ChaptersAdapter(List<ChapterModel> list, OnClick onClick) {
        this.list = list;
        this.onClick = onClick;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle, tvPages, tvChapterNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPages = itemView.findViewById(R.id.tvPages);
            tvChapterNumber = itemView.findViewById(R.id.tvChapterNumber);
        }
    }


    @NonNull
    @Override
    public ChaptersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapters_item, parent, false);
        return new ChaptersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChaptersAdapter.ViewHolder holder, int position) {
        ChapterModel item = list.get(position);

        holder.tvChapterNumber.setText("Vol " + item.getVolume() + " Chapter " + item.getChapter());
        holder.tvTitle.setText(item.getTitle());
        holder.tvPages.setText(item.getPages() + " pages");

        holder.itemView.setOnClickListener(v -> onClick.OnClickListener(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnClick {
        void OnClickListener(ChapterModel chapter);
    }
}


