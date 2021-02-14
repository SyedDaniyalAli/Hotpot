package com.sda.syeddaniyalali.hotpot_;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseServices extends Service {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef2 = database.getReference("Orders");


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Read from the database
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showNotification("Please Check Order","There is some new Order");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(FirebaseServices.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotification(String title, String message)
    {
        Intent intent=new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 333, intent, 0);


//        Notification notification = new NotificationCompat.Builder(this, "MyNotifications")
//                .setContentTitle(title)
//                .setContentText(message)
//                .setContentTitle(title)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(333, notification);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotifications")
                .setSmallIcon(R.mipmap.ic_launcher_background)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(333, builder.build());
    }

}
