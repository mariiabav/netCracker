package com.example.problemsolver;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;

public class MyFCMClass extends FirebaseMessagingService {

    private final String TAG = "JSA-FCM";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Title: " + remoteMessage.getNotification().getTitle());
            Log.e(TAG, "Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data: " + remoteMessage.getData());
        }
    }
}
