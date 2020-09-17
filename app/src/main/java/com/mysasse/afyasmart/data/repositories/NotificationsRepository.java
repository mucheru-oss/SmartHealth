package com.mysasse.afyasmart.data.repositories;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.data.models.Notification;

import java.util.List;

public class NotificationsRepository {
    private static final String TAG = "NotificationsRepository";
    private FirebaseFirestore mDatabase;
    private GetNotificationsTaskListener listener;

    public NotificationsRepository(GetNotificationsTaskListener listener) {
        this.listener = listener;
        mDatabase = FirebaseFirestore.getInstance();
    }

    public void getAllNotificationsFromFirebase() {
        mDatabase.collection("notifications")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Log.e(TAG, "getAllNotificationsFromFirebase: error; ", e);
                        listener.onError(e);
                        return;
                    }

                    assert queryDocumentSnapshots != null;
                    Log.d(TAG, "getAllNotificationsFromFirebase: notifications: count" + queryDocumentSnapshots.size());
                    listener.onComplete(queryDocumentSnapshots.toObjects(Notification.class));

                });
    }

    public void deleteNotificationFromFirebase(Notification notification) {
        mDatabase.collection("notifications")
                .document(notification.getId())
                .delete().
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "deleteNotificationFromFirebase: notification deleted");
                        getAllNotificationsFromFirebase();
                    } else {
                        Log.e(TAG, "deleteNotificationFromFirebase: ", task.getException());
                        listener.onError(task.getException());
                    }
                });
    }


    public interface GetNotificationsTaskListener {
        void onError(Exception exception);

        void onComplete(List<Notification> notifications);

    }
}
