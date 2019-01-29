package com.example.camil.detectnalert;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class Notification extends FirebaseMessagingService {

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //This will give you the topic string from curl request (/topics/news)
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //This will give you the Text property in the curl request(Sample Message):
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //This is where you get your click_action
        Log.d(TAG, "Notification Click Action: " + remoteMessage.getNotification().getClickAction());
        //put code here to navigate based on click_action
    }
}
