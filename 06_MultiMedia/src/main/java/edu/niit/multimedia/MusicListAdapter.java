package edu.niit.multimedia;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.MyViewHolder> {

    private Context context;
    private List<File> files;
    private OnItemClickListener itemClickListener;
    private int selectedItem = -1;

    public MusicListAdapter(Context context, List<File> files) {
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        // MediaMetadataRetriever获取mp3文件的内容，包括作者、图片等信息
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(files.get(position).getAbsolutePath());
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if(title != null) {
            holder.tvMusicName.setText(title);
        } else {
            holder.tvMusicName.setText(files.get(position).getName());
        }
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        if(duration != null) {
            Date date = new Date(Integer.valueOf(duration));
            SimpleDateFormat formatDuration = new SimpleDateFormat("mm:ss");
            holder.tvDuration.setText("时长：" + formatDuration.format(date));
        } else {
            holder.tvDuration.setText("时长：未知");
        }
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });
        if(selectedItem == position) {
            holder.itemLayout.setBackgroundColor(context.getResources()
                    .getColor(R.color.background));
        } else {
            holder.itemLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_music_name)
        TextView tvMusicName;
        @BindView(R.id.tv_duration)
        TextView tvDuration;
        @BindView(R.id.item_layout)
        LinearLayout itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setSelectedBackground(int position) {
        this.selectedItem = position;
        notifyDataSetChanged();
    }

    // 定义事件监听的接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
