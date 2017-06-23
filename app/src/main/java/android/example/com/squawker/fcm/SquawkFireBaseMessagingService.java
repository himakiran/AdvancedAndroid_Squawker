package android.example.com.squawker.fcm;

import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.R.attr.author;
import static android.R.id.message;
import static android.example.com.squawker.provider.SquawkProvider.AUTHORITY;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


/**
 * Created by userhk on 23/06/17.
 */

public class SquawkFireBaseMessagingService extends FirebaseMessagingService {

    private final String LOG_TAG = SquawkFireBaseMessagingService.class.getSimpleName();
    private String author;
    private String test;
    private String authorKey;
    private String msg;
    private String date;

    @Override
    public void onMessageReceived(RemoteMessage message) {

        Map<String, String> data = message.getData();

        //insert msg in db

        insertSquawk();
        displayNotfcn(data);

    }

    private void displayNotfcn(Map<String, String> data){
        author = data.get("author");
        test = data.get("test");
        authorKey = data.get("authorKey");
        msg = data.get("message");
        date = data.get("date");

        // Switch statement to select author icon
        int icon;
        int mId = 1;
        switch (author) {
            case "asser":
                icon = R.drawable.asser;
                break;
            case "cezanne":
                icon = R.drawable.cezanne;
                break;
            case "jlin":
                icon = R.drawable.jlin;
                break;
            case "lyla":
                icon = R.drawable.lyla;
                break;
            case "nikita":
                icon = R.drawable.nikita;
                break;
            default:
                icon = R.drawable.test;
                break;
        }
        // Displaying a notification

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(author)
                .setContentText(msg);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());



    }

    // insert msgs into db
     private void insertSquawk() {
         // Async task defn to update the database with msg

         AsyncTask<Void, Void, Void> updateDBTask = new AsyncTask<Void, Void, Void>() {


             Uri uri = Uri.parse("content://" + AUTHORITY + "/messages");

             // Invoked on a background thread
             @Override
             protected Void doInBackground(Void... voids) {

                 // Get the content resolver
                 ContentResolver resolver = getContentResolver();
                 ContentValues values = new ContentValues();
                 values.put(SquawkContract.COLUMN_AUTHOR, author);
                 values.put(SquawkContract.COLUMN_AUTHOR_KEY, authorKey);
                 values.put(SquawkContract.COLUMN_MESSAGE, message);
                 values.put(SquawkContract.COLUMN_DATE, date);


                 resolver.insert(uri, values);
                 return null;


             }
         };

         updateDBTask.execute();

     }











}
