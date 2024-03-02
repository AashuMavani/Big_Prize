package com.app.bigprize.Async;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.RemoteViews;

import com.app.bigprize.Activity.Big_SplashScreenActivity;
import com.app.bigprize.R;
import com.app.bigprize.utils.BIG_Common_Utils;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class Big_GeneratePictureStyleNotificationAsync extends AsyncTask<String, Void, Bitmap> {
    private final String bundle;
    private Context context;
    private String title, message, imageUrl, mType, points, btnName, btnColor;
    private Notification notif = null;
    private boolean mCloseForce;

    public Big_GeneratePictureStyleNotificationAsync(Context context, String type, String title, String message, String imageUrl, boolean ivClose, String bundle) {
        super();
        this.context = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.bundle = bundle;
        this.mType = type;
        this.mCloseForce = ivClose;
        try {
            this.points = new JSONObject(bundle).getString("Rubies");
            this.btnName = new JSONObject(bundle).getString("btnName");
            this.btnColor = new JSONObject(bundle).getString("btnColor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        InputStream in;
        try {
            URL url = new URL(this.imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(300000);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        try {
            JSONObject jsonData = new JSONObject(bundle);
            Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.big_ic_launcher);
            int stateIconName = R.drawable.ic_stat_name_new;
            if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("fire")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_fire);
                stateIconName = R.drawable.big_ic_fire;
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("playtime")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_playtime);
                stateIconName = R.drawable.big_ic_playtime;
//            } else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("paytm")) {
//                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_paytm);
//                stateIconName = R.drawable.big_ic_paytm;
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("upi")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_upi);
                stateIconName = R.drawable.big_ic_upi;
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("code")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_redeem_code);
                stateIconName = R .drawable.big_ic_redeem_code;
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("whatsapp")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_whatsapp1);
                stateIconName = R.drawable.big_ic_whatsapp1;
            }else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("success")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_success);
                stateIconName= R.drawable.big_ic_success;
            }          else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("free")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_free);
                stateIconName = R.drawable.big_ic_free;
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("fast_withdraw")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_fast_withdraw);
                stateIconName = R.drawable.big_ic_fast_withdraw;
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("instant_withdraw")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_instant_withdraw);
                stateIconName = R.drawable.big_ic_instant_withdraw;
            } else if (!BIG_Common_Utils.isStringNullOrEmpty(jsonData.getString("icon")) && jsonData.getString("icon").equals("loot_now")) {
                icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.big_ic_loot_now);
                stateIconName = R.drawable.big_ic_loot_now;
            }




            Intent notificationIntent = new Intent(context, Big_SplashScreenActivity.class);
            notificationIntent.putExtra("bundle", bundle);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            int notificationid = 0;
            if (new JSONObject(bundle).getString("Notification_Id") != null && new JSONObject(bundle).getString("Notification_Id").trim().length() > 0) {
                notificationid = Integer.parseInt(new JSONObject(bundle).getString("Notification_Id"));
            } else {
                Random r = new Random();
                notificationid = r.nextInt(500);
            }
            PendingIntent intent = PendingIntent.getActivity(context, notificationid, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            ////AppLogger.getInstance().e("mType--)", "" + mType + "--)" + title + "--)" + message + "--)" + imageUrl + "--)" + result);
            if (mType.matches("1")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("channel_01", title, NotificationManager.IMPORTANCE_HIGH);
                    channel.setSound(null, null);
                    notificationManager.createNotificationChannel(channel);
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setLargeIcon(icon1)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .setChannelId("channel_01")
                            .setStyle(new Notification.BigPictureStyle().bigPicture(result).setSummaryText(message))
                            .build();
                } else {
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setLargeIcon(icon1)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .setStyle(new Notification.BigPictureStyle().bigPicture(result).setSummaryText(message))
                            .build();
                }
                notif.defaults |= Notification.DEFAULT_VIBRATE;
                notif.defaults |= Notification.DEFAULT_SOUND;
                notificationManager.notify(notificationid, notif);
            } else if (mType.matches("2")) {
                RemoteViews contentViewSmallTop = new RemoteViews(context.getPackageName(), R.layout.big_custom_notification_top);
                contentViewSmallTop.setTextViewText(R.id.txtNotiTitle, title);
                contentViewSmallTop.setTextViewText(R.id.txtNotiDesc, message);
                contentViewSmallTop.setImageViewBitmap(R.id.ivNotiImageBig, result);

                RemoteViews contentViewBigTop = new RemoteViews(context.getPackageName(), R.layout.big_custom_notification_top_big);
                contentViewBigTop.setTextViewText(R.id.txtNotiTitleBig, title);
                contentViewBigTop.setTextViewText(R.id.txtNotiDescBig, message);
                contentViewBigTop.setImageViewBitmap(R.id.ivNotiImageBig, result);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    ////AppLogger.getInstance().e("notifi--)", "yes");
                    NotificationChannel channel = new NotificationChannel("channel_01", title, NotificationManager.IMPORTANCE_HIGH);
                    channel.setSound(null, null);
                    notificationManager.createNotificationChannel(channel);
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewSmallTop)
                            .setCustomBigContentView(contentViewBigTop)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .setChannelId("channel_01")
                            .build();
                } else {
                    ////AppLogger.getInstance().e("notifi--)", "yes1");
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewSmallTop)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setLargeIcon(icon1)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .build();
                }
                notif.defaults |= Notification.DEFAULT_VIBRATE;
                notif.defaults |= Notification.DEFAULT_SOUND;
                notificationManager.notify(notificationid, notif);
            } else if (mType.matches("3")) {
                RemoteViews contentViewSmallBtm = new RemoteViews(context.getPackageName(), R.layout.big_custom_notification_top);
                contentViewSmallBtm.setTextViewText(R.id.txtNotiTitle, title);
                contentViewSmallBtm.setTextViewText(R.id.txtNotiDesc, message);
                contentViewSmallBtm.setImageViewBitmap(R.id.ivNotiImageBig, result);

                RemoteViews contentViewBigBtm = new RemoteViews(context.getPackageName(), R.layout.big_custom_notification_btm_big);
                contentViewBigBtm.setTextViewText(R.id.txtNotiTitleBig, title);
                contentViewBigBtm.setTextViewText(R.id.txtNotiDescBig, message);
                contentViewBigBtm.setImageViewBitmap(R.id.ivNotiImageBig, result);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    ////AppLogger.getInstance().e("notifi--)", "yes");
                    NotificationChannel channel = new NotificationChannel("channel_01", title, NotificationManager.IMPORTANCE_HIGH);
                    channel.setSound(null, null);
                    notificationManager.createNotificationChannel(channel);
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewSmallBtm)
                            .setCustomBigContentView(contentViewBigBtm)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .setChannelId("channel_01")
                            .build();
                } else {
                    ////AppLogger.getInstance().e("notifi--)", "yes1");
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewSmallBtm)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setLargeIcon(icon1)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .build();
                }
                notif.defaults |= Notification.DEFAULT_VIBRATE;
                notif.defaults |= Notification.DEFAULT_SOUND;
                notificationManager.notify(notificationid, notif);
            } else if (mType.matches("4")) {
                RemoteViews contentViewSmallBtm = new RemoteViews(context.getPackageName(), R.layout.big_custom_notification_only_image);
                contentViewSmallBtm.setImageViewBitmap(R.id.ivSmallImage, result);

                RemoteViews contentViewBigBtm = new RemoteViews(context.getPackageName(), R.layout.big_custom_notification_btm_big);
                contentViewBigBtm.setTextViewText(R.id.txtNotiTitleBig, title);
                contentViewBigBtm.setTextViewText(R.id.txtNotiDescBig, message);
                contentViewBigBtm.setImageViewBitmap(R.id.ivNotiImageBig, result);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    ////AppLogger.getInstance().e("notifi4--)", "yes");
                    NotificationChannel channel = new NotificationChannel("channel_01", title, NotificationManager.IMPORTANCE_HIGH);
                    channel.setSound(null, null);
                    notificationManager.createNotificationChannel(channel);
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewSmallBtm)
                            .setCustomBigContentView(contentViewBigBtm)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .setChannelId("channel_01")
                            .build();
                } else {
                    ////AppLogger.getInstance().e("notifi--)", "yes1");
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewSmallBtm)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setLargeIcon(icon1)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .build();
                }
                notif.defaults |= Notification.DEFAULT_VIBRATE;
                notif.defaults |= Notification.DEFAULT_SOUND;
                notificationManager.notify(notificationid, notif);
            } else if (mType.matches("5")) {
                RemoteViews contentViewBigBtm = new RemoteViews(context.getPackageName(), R.layout.big_custom_notification_task);
                contentViewBigBtm.setTextViewText(R.id.txtNotiTitleBig, title);
                contentViewBigBtm.setTextViewText(R.id.txtNotiDescBig, message);
                if (!BIG_Common_Utils.isStringNullOrEmpty(points)) {
                    contentViewBigBtm.setTextViewText(R.id.txtPoint, points);
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(btnName)) {
                    contentViewBigBtm.setTextViewText(R.id.txtInstall, btnName);
                }
                if (!BIG_Common_Utils.isStringNullOrEmpty(btnColor)) {
                    contentViewBigBtm.setInt(R.id.txtInstall, "setBackgroundColor", Color.parseColor("#" + btnColor));
                }
                contentViewBigBtm.setImageViewBitmap(R.id.ivNotiImageBig, result);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    ////AppLogger.getInstance().e("notifi4--)", "yes");
                    NotificationChannel channel = new NotificationChannel("channel_01", title, NotificationManager.IMPORTANCE_HIGH);
                    channel.setSound(null, null);
                    notificationManager.createNotificationChannel(channel);
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewBigBtm)
                            .setCustomBigContentView(contentViewBigBtm)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .setChannelId("channel_01")
                            .build();
                } else {
                    ////AppLogger.getInstance().e("notifi--)", "yes1");
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewBigBtm)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setLargeIcon(icon1)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .build();
                }
                notif.defaults |= Notification.DEFAULT_VIBRATE;
                notif.defaults |= Notification.DEFAULT_SOUND;
                notificationManager.notify(notificationid, notif);
            } else if (mType.matches("6")) {
                RemoteViews contentViewSmallBtm = new RemoteViews(context.getPackageName(), R.layout.big_custom_notification_top);
                contentViewSmallBtm.setTextViewText(R.id.txtNotiTitle, title);
                contentViewSmallBtm.setTextViewText(R.id.txtNotiDesc, message);
                contentViewSmallBtm.setImageViewBitmap(R.id.ivNotiImageBig, result);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ////AppLogger.getInstance().e("notifi--)", "yes");
                    NotificationChannel channel = new NotificationChannel("channel_01", title, NotificationManager.IMPORTANCE_HIGH);
                    channel.setSound(null, null);
                    notificationManager.createNotificationChannel(channel);
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewSmallBtm)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .setChannelId("channel_01")
                            .build();
                } else {
                    ////AppLogger.getInstance().e("notifi--)", "yes1");
                    notif = new Notification.Builder(context)
                            .setContentTitle(title)
                            .setContent(contentViewSmallBtm)
                            .setSmallIcon(stateIconName)
                            .setContentIntent(intent)
                            .setLargeIcon(icon1)
                            .setAutoCancel(mCloseForce)
                            .setOngoing(mCloseForce)
                            .build();
                }
                notif.defaults |= Notification.DEFAULT_VIBRATE;
                notif.defaults |= Notification.DEFAULT_SOUND;
                notificationManager.notify(notificationid, notif);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
