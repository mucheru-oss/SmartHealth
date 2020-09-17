package com.mysasse.afyasmart.ui.fragments.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Notification;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {

    private NotificationClickListener listener;
    private List<Notification> notifications;

    public NotificationsAdapter(NotificationClickListener listener, List<Notification> notifications) {
        this.listener = listener;

        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.titleTv.setText(notification.getTitle());
        holder.bodyTv.setText(notification.getBody());

        holder.mView.setOnClickListener(view -> listener.onClick(notification));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        TextView bodyTv;
        View mView;

        NotificationsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            titleTv = itemView.findViewById(R.id.title_tv);
            bodyTv = itemView.findViewById(R.id.body_tv);
        }
    }

    public interface NotificationClickListener {
        void onClick(Notification notification);
    }
}
