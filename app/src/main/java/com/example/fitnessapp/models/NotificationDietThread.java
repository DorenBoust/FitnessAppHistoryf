package com.example.fitnessapp.models;


import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.fitnessapp.R;
import com.example.fitnessapp.keys.KeysSharedPrefercence;
import com.example.fitnessapp.user.Meal;
import com.example.fitnessapp.user.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.fitnessapp.models.AppNotification.CHANNEL_2_ID;

public class NotificationDietThread extends Thread {

    private List<Meal> meals;
    private Context context;
    private NotificationManagerCompat notificationManager;

    public NotificationDietThread(Context context, NotificationManagerCompat notificationManager, User user){

        this.context = context;
        this.notificationManager = notificationManager;
        this.meals = user.getDiet().getMeals();

    }

    @Override
    public void run() {

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String correctTime = timeFormat.format(new Date());

        System.out.println(correctTime);

        int sharedPrefreranceMealNotification = getSharedPrefreranceMealNotification();
        System.out.println("sharedPrefreranceMealNotification - " + sharedPrefreranceMealNotification);


        for (int i = 0; i < meals.size() ; i++) {
            Meal meal = meals.get(i);

            String name = meal.getName();
            String time = meal.getTime();


            if ((correctTime.equals(time)) && (sharedPrefreranceMealNotification == 1)){

                notification(time,name);

                try {
                    Thread.sleep(60_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                run();

            }

        }


        try {
            Thread.sleep(60_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        run();



    }


    private void notification(String mealTime, String mealName){

        String contentText = mealTime + " - " + mealName + " -" + " הגיע הזמן לאכול ";

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_notification_ex_timer)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(1, notification);
    }

    private int getSharedPrefreranceMealNotification(){


        SharedPreferences sharedPreferences = context.getSharedPreferences(KeysSharedPrefercence.USER_SHAREDPREFERCENCE_NAME, MODE_PRIVATE);

        return sharedPreferences.getInt(KeysSharedPrefercence.DIET_NOTIFICATION_SWITCH, 1);

    }

}



