package com.mysasse.afyasmart.ui.fragments.notifications;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysasse.afyasmart.data.models.Notification;
import com.mysasse.afyasmart.data.repositories.NotificationsRepository;

import java.util.List;

public class NotificationsViewModel extends ViewModel implements NotificationsRepository.GetNotificationsTaskListener {
    private static final String TAG = "NotificationsViewModel";

    private NotificationsRepository notificationsRepository;
    private MutableLiveData<List<Notification>> _notifications;

    public NotificationsViewModel() {
        _notifications = new MutableLiveData<>();
        notificationsRepository = new NotificationsRepository(this);
    }

    public LiveData<List<Notification>> getAllNotifications() {
        notificationsRepository.getAllNotificationsFromFirebase();
        return _notifications;
    }

    @Override
    public void onError(Exception exception) {
        Log.e(TAG, "onError: view model: ", exception);
    }

    @Override
    public void onComplete(List<Notification> notifications) {
        _notifications.setValue(notifications);
    }

    public void deleteNotification(Notification notification) {
        assert notification != null;

        notificationsRepository.deleteNotificationFromFirebase(notification);

    }
}
