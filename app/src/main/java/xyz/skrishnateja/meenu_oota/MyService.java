package xyz.skrishnateja.meenu_oota;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {

     String a = "Meenu-oota",b="pls connect to internet",c="network error";

    DBHelper mydb;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        new DownloadTask().execute("http://www.skrishnateja.xyz/notify.php");

        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new DownloadTask().execute("http://www.skrishnateja.xyz/notify.php");
        return super.onStartCommand(intent, flags, startId);
    }

    public void sendNotification() {


        Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);


        builder.setSmallIcon(R.drawable.image6);

        builder.setContentIntent(pendingIntent);

        builder.setAutoCancel(true);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.image6));

        builder.setContentTitle(a);
        builder.setContentText(b);
        builder.setSubText(c);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
//                Toast.makeText(getApplicationContext(),"i hav called",Toast.LENGTH_SHORT).show();
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return getString(R.string.connection_error);
            }
        }

        @Override
        protected void onPreExecute() {
     //       Toast.makeText(getApplicationContext(), "i hav called", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
           // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            sendNotification();
            Log.i(a,b);
          //  Toast.makeText(getApplicationContext(),a+b+c,Toast.LENGTH_SHORT).show();
        }
    }




    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";

        try {
            Log.i("LOADFROMNETWORK", "DOING");
            stream = downloadUrl(urlString);
            Log.i("LOADFROMNETURL","DONE");
            str = readIt(stream, 3000);

//            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
            try {


                JSONArray ja = new JSONArray(str);
                JSONObject jo = null;

               // b = str;

                Log.i("I'm Printing JSON",ja.length()+ja.toString());
                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);

                    Log.i("JSON Object",jo.toString());
                    a = jo.getString("name");
                    b = jo.getString("details");
                    c = jo.getString("more");

                 //    Toast.makeText(getApplicationContext(), jo.getString("quantity"), Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                android.util.Log.e("Webservice 3", e.toString());
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        Log.i("I did it ", str);
//        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        return str;
    }
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();

        InputStream stream = conn.getInputStream();
        return stream;
    }
    private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
