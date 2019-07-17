package com.letter.days.anniversary;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.letter.days.R;

import java.util.List;

public class AnniAdapter extends RecyclerView.Adapter<AnniAdapter.ViewHolder> {

    private OnAnniItemClickListener onAnniItemClickListener;

    private Context mContext;
    private List<Anniversary> mAnniversaryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View anniView;
        TextView anniDate;
        TextView anniDays;
        TextView anniText;
        TextView anniType;
        ProgressBar anniProgress;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            anniView = itemView;
            anniDate = itemView.findViewById(R.id.anni_date);
            anniDays = itemView.findViewById(R.id.anni_days);
            anniText = itemView.findViewById(R.id.anni_text);
            anniType = itemView.findViewById(R.id.anni_type);
            anniProgress = itemView.findViewById(R.id.anni_progress);
        }
    }

    public AnniAdapter(List<Anniversary> anniversaryList) {
        mAnniversaryList = anniversaryList;
    }

    public void setOnAnniItemClickListener (OnAnniItemClickListener onAnniItemClickListener) {
        this.onAnniItemClickListener = onAnniItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.anniversary_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.anniView.setOnClickListener((v) -> {
                int position = holder.getAdapterPosition();
                onAnniItemClickListener.onItemClick(position);
            });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anniversary anniversary = mAnniversaryList.get(position);
        holder.anniDays.setText(anniversary.getDaysText());
        holder.anniDate.setText(anniversary.getDateText(mContext.getString(R.string.date_format_split),
                mContext.getString(R.string.date_lunar_format)));
        holder.anniText.setText(anniversary.getText());
        holder.anniType.setText(anniversary.getTypeText());
        setProgressColor(holder.anniProgress, anniversary.getColor());
        int progress = 366;
        if (anniversary.getNextTime() > 366) {
            progress = 0;
        } else if (anniversary.getNextTime() > 0) {
            progress = 366 - (int)anniversary.getNextTime();
        }
//        holder.anniProgress.setProgress(progress);
        ObjectAnimator.ofInt(holder.anniProgress, "progress", 0, progress)
                .setDuration(500)
                .start();
    }

    @Override
    public int getItemCount() {
        return mAnniversaryList.size();
    }

    private void setProgressColor(@NonNull ProgressBar progressBar, int color) {
        LayerDrawable drawable = (LayerDrawable) progressBar.getProgressDrawable();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        ClipDrawable clipDrawable = new ClipDrawable(gradientDrawable, Gravity.START, ClipDrawable.HORIZONTAL);
        drawable.setDrawableByLayerId(android.R.id.progress, clipDrawable);
        progressBar.setProgressDrawable(drawable);
    }

}

