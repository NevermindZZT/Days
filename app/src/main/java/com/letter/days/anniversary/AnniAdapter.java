package com.letter.days.anniversary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.letter.days.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class AnniAdapter extends RecyclerView.Adapter<AnniAdapter.ViewHolder> {

    private OnAnniItemClickListener onAnniItemClickListener;

    private Context mContext;
    private List<Anniversary> mAnniversaryList;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    static class ViewHolder extends RecyclerView.ViewHolder {
        View anniView;
        TextView anniDate;
        TextView anniDays;
        TextView anniText;
        TextView anniType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anniView = itemView;
            anniDate = itemView.findViewById(R.id.anni_date);
            anniDays = itemView.findViewById(R.id.anni_days);
            anniText = itemView.findViewById(R.id.anni_text);
            anniType = itemView.findViewById(R.id.anni_type);
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
        holder.anniView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                onAnniItemClickListener.onItemClick(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anniversary anniversary = mAnniversaryList.get(position);
        holder.anniDays.setText(anniversary.getDaysText());
        holder.anniDate.setText(format.format(anniversary.getTime()));
        holder.anniText.setText(anniversary.getText());
        holder.anniType.setText(anniversary.getTypeText());
    }

    @Override
    public int getItemCount() {
        return mAnniversaryList.size();
    }

}

