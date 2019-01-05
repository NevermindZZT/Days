package com.letter.days.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.letter.days.R;
import com.letter.days.anniversary.Anniversary;
import com.letter.days.anniversary.OnAnniItemClickListener;

import java.util.List;

public class WidgetInfoAdapter extends RecyclerView.Adapter<WidgetInfoAdapter.ViewHolder> {

    private OnAnniItemClickListener onAnniItemClickListener;

    private Context mContext;
    private List<Anniversary> mAnniversaryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View widgetItemView;
        TextView widgetName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            widgetItemView = itemView;
            widgetName = itemView.findViewById(R.id.widget_name);
        }
    }

    public WidgetInfoAdapter(List<Anniversary> anniversaryList) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_info_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.widgetItemView.setOnClickListener(new View.OnClickListener() {
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
        holder.widgetName.setText(anniversary.getText());
    }

    @Override
    public int getItemCount() {
        return mAnniversaryList.size();
    }
}
