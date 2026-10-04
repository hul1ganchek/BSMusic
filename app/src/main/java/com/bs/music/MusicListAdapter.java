package com.bs.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    Context context;
    private ViewHolder holder;
    ArrayList<AudioModel> songsList;

    public MusicListAdapter(ArrayList<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        AudioModel songData = this.songsList.get(position);
        holder.titleTextView.setText(songData.getTitle());
        if (MyMediaPlayer.currentIndex == position) {
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.titleTextView.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMediaPlayer.currentIndex = position;
                android.content.Intent intent = new android.content.Intent(view.getContext(), MusicPlayerActivity.class);
                intent.putExtra("LIST", songsList);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.songsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView) itemView.findViewById(R.id.music_title_text);
            this.iconImageView = (ImageView) itemView.findViewById(R.id.icon_view);
        }
    }
}
