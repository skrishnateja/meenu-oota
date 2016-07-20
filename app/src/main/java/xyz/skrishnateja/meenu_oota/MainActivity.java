package xyz.skrishnateja.meenu_oota;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity {

    List<String> list1, list2;
    private String TAG = ":::::::::::KT::::::::::::";
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DownloadTask().execute("http://www.skrishnateja.xyz/jsonscript.php");

        update();
        clicked();
        ArrayList array_list = mydb.getAllFish();
        String[] itemname = {"","","","","",""};
        Integer imgid[] = {R.drawable.img,R.drawable.img1,R.drawable.img2,R.drawable.img,R.drawable.img1,R.drawable.img2};
        for (int i = 0;i<array_list.size();i++) {
            itemname[i] = (String) array_list.get(i);
        }
        ArrayList array_list1 = mydb.getPriceFish();
        String[] itemname1 = {"","","","","",""};
        for (int i = 0;i<array_list.size();i++) {
            itemname1[i] = "Price "+(String) array_list1.get(i);
        }



        CustomListAdapter adapter=new CustomListAdapter(this, itemname, itemname1, imgid);
        ListView list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);




        startService(new Intent(getBaseContext(), MyService.class));
    }

    private void update() {

        mydb = new DBHelper(this);

//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);
//
//        ListView obj = (ListView)findViewById(R.id.listView);
//        obj.setAdapter(arrayAdapter);
//        //mydb.insertFav("1");


//        TextView tv = (TextView) findViewById(R.id.textView);
//        tv.setText(mydb.getFav("1")+":"+mydb.getFav("2"));
        Log.i("----------FULL FISH",mydb.getAllFish().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_refresh) {
            new DownloadTask().execute("http://www.skrishnateja.xyz/jsonscript.php");
            return true;
        }

        return super.onOptionsItemSelected(item);
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
           // Toast.makeText(getApplicationContext(),"i hav called",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
          //  Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            update();
        }
    }
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";

        try {
            Log.i("LOADFROMNETWORK","DOING");
            stream = downloadUrl(urlString);
            Log.i("LOADFROMNETURL","DONE");
            str = readIt(stream, 3000);

//            Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
            try {


                JSONArray ja = new JSONArray(str);
                JSONObject jo = null;

                list1 = new ArrayList<String>();
                list2 = new ArrayList<String>();

                Log.i("I'm Printing JSON",ja.length()+ja.toString());
                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);

                    Log.i("JSON Object",jo.toString());
                    list1.add(jo.getString("name"));
                    list2.add(jo.getString("detalis"));
                    if(mydb.updateFish(jo.getString("id"), jo.getString("name"), jo.getString("detalis"),
                            jo.getString("quantity"), jo.getString("marketname"), jo.getString("vendorname"),
                            jo.getString("price"))){
                     //   TextView tv = (TextView) findViewById(R.id.textView);
                       // tv.setText("I did it"+i);
                        Log.i("Row added:::",jo.getString("id"));
                    }
                    //TextView tv = (TextView) findViewById(R.id.textView);
                    //tv.setText(jo.getString("quantity"));
                   // Toast.makeText(getApplicationContext(), jo.getString("quantity"), Toast.LENGTH_SHORT).show();
                }

                Log.i(TAG,list1.toString());
                Log.i(TAG, list2.toString());
            } catch (Exception e) {
                android.util.Log.e("Webservice 3", e.toString());
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        Log.i("I did it ", str);
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

    public void clicked() {
        ListView list = (ListView) findViewById(R.id.listView);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent nextScreen = new Intent(getApplicationContext(), Main2Activity.class);


                //fish ifish = myfishes.get(position);
                //Sending data to another Activity

                // nextScreen.putExtra("name", );
                String pos = Integer.toString(position+1);
                    nextScreen.putExtra("id",pos);
                startActivity(nextScreen);

            }
        });
    }


}
