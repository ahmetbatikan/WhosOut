package com.example.asuspc.whosout;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service{
    Context context ;
    Notification notification;
    Timer timer;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {//Servis startService(); metoduyla çağrıldığında çalışır
        context = getApplicationContext();

        timer = new Timer();
        timer.schedule(new TimerTask() {  //her 60 sn de bir bildirimGonder(); metodu çağırılır.
            @Override
            public void run() {
                new DatabaseTask().execute();
            }
        }, 0, 10000);

    }
    private void addNotification(int[] arr) {
        /*long when = System.currentTimeMillis();//notificationın ne zaman gösterileceği
        String baslik = "mobilhanem.com";
        NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(context,SplashActivity.class);
        PendingIntent  pending=PendingIntent.getActivity(context, 0, intent, 0);//Notificationa tıklanınca açılacak activityi belirliyoruz
        Notification notification;
        notification = new Notification(R.id.splash_image, "Yeni Bildirim", when);
        notification.bu(context,baslik,("Security: " + arr[2] + ", Visitor: " + arr[1]),pending);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;//notificationa tıklanınca notificationın otomatik silinmesi için
        notification.defaults |= Notification.DEFAULT_SOUND;//notification geldiğinde bildirim sesi çalması için
        notification.defaults |= Notification.DEFAULT_VIBRATE;//notification geldiğinde bildirim titremesi için
        nm.notify(0, notification);*/
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.splash_img)
                        .setContentTitle("Check the app for comers!")
                        .setContentText("Security: " + arr[2] + ", Visitor: " + arr[1]);

        Intent notificationIntent;
        if(arr[2]>=arr[1]){
            notificationIntent= new Intent(this, SecurityActivity.class);
        }
        else{
            notificationIntent= new Intent(this, VisitorActivity.class);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        Log.d("Notify ","running");

    }

    private class DatabaseTask extends AsyncTask<Void, Void, Void> {
        DatabaseHelper helper;
        int[] arr;
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                helper = new DatabaseHelper();
                helper.connectDatabase();
                //while (true) {
                arr=helper.checkNewSecurity();
                //TODO: check the condition in the splash!!
                //Thread.sleep(2000);


                //}
            }
            catch (Exception e) {
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if (arr[0]>0) {
                addNotification(arr);
            }
        }
    }
    @Override
    public void onDestroy() {//Servis stopService(); metoduyla durdurulduğunda çalışır
        timer.cancel();
    }

}
