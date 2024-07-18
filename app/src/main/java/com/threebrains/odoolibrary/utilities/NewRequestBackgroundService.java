package com.threebrains.odoolibrary.utilities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.SplashActivity;

import java.util.List;


public class NewRequestBackgroundService extends Service {

    FirebaseFirestore fbStore;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nfc = new NotificationChannel("NewRequestChannel", "NewRequestNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nfm = getSystemService(NotificationManager.class);
            nfm.createNotificationChannel(nfc);
        }
        Notification notification = new NotificationCompat.Builder(this, "NewRequestChannel")
                .setContentTitle("New Request Added!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(Color.rgb(255, 152, 0))
                .build();
        fbStore = FirebaseFirestore.getInstance();
        fbStore.collection("requested").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentChange> dcs  = value.getDocumentChanges();
                for(DocumentChange dc : dcs){
                   DocumentSnapshot ds = dc.getDocument();
                   if(ds.getString("status").equals("pending")){
                       generateNotification("New book request · "+ds.getString("title"), ds.getString("username"));
                   }
                }
            }
        });
        startForeground(2004, notification);
        return super.onStartCommand(intent, flags, startId);
    }
    @SuppressLint("MissingPermission")
    public void generateNotification(String title, String message) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nfc = new NotificationChannel("NewRequestChannel", "NewRequestNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nfm = getSystemService(NotificationManager.class);
            nfm.createNotificationChannel(nfc);
        }
        try{
            NotificationCompat.Builder bl = new NotificationCompat.Builder(NewRequestBackgroundService.this,"NewRequestChannel");
            bl.setContentTitle(title);
            bl.setContentText(message);
            bl.setAutoCancel(false);
            bl.setSmallIcon(R.drawable.ic_launcher_foreground);
            Intent in = new Intent(NewRequestBackgroundService.this, SplashActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, in, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            bl.setContentIntent(pi);
            bl.setAutoCancel(true);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(NewRequestBackgroundService.this);
            managerCompat.notify(2004, bl.build());
        } catch (Exception e) {

        }
    }
}
