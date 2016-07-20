package xyz.skrishnateja.meenu_oota;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private static Context sContext;
    ArrayList array_list;
    public static int i=0;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sContext = getApplicationContext();
        DBHelper mydb = new DBHelper(sContext);
        Intent i = getIntent();
        String id = i.getStringExtra("id");
        array_list = mydb.getoneFish(id);
        //ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);
        TextView tv1 = (TextView) findViewById(R.id.name);
        tv1.setText((String) array_list.get(0));
        TextView tv3 = (TextView) findViewById(R.id.textView2);
        tv3.setText("Description: " + (String) array_list.get(1));
        TextView tv2 = (TextView) findViewById(R.id.textView3);
        tv2.setText("Available Market: " + array_list.get(2));
        TextView tv5 = (TextView) findViewById(R.id.textView5);
        tv5.setText("Price: " + array_list.get(5));
        RatingBar tv4 = (RatingBar) findViewById(R.id.ratingBar);
        tv4.setRating(2.5f);
        img = (ImageView) findViewById(R.id.imageView);
        new LoadImage().execute("http://skrishnateja.xyz/images/img" + id+".jpg");


        int is = mydb.getFav(id);
        if (is==0){
            Button tv = (Button) findViewById(R.id.fav);
            tv.setText("Remove Fav");
        }
        //ListView obj = (ListView)findViewById(R.id.listView1);
       // obj.setAdapter(arrayAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void fav(View view) {
        Intent i = getIntent();
        String id = i.getStringExtra("id");
        sContext = getApplicationContext();
        DBHelper mydb = new DBHelper(sContext);
        int is = mydb.getFav(id);
        if (is==0){
            Button tv = (Button) findViewById(R.id.fav);
            tv.setText("Remove Fav");
            mydb.insertFav(id);
        }
        else {
            mydb.deleteFav(id);
            Button tv = (Button) findViewById(R.id.fav);
            tv.setText("Fav");
        }
    }
    public void change(View v){

        if (i==1){
            Button tv2 = (Button) findViewById(R.id.textView3);
            tv2.setText("Available Market: "+ array_list.get(2));
            i=0;
        }
        else {
            Button tv2 = (Button) findViewById(R.id.textView3);
            tv2.setText("Contact Person: " + array_list.get(3));
            i=1;
        }
    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Main2Activity.this);
            pDialog.setMessage("Loading Fish ....");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                img.setImageBitmap(image);
                pDialog.dismiss();

            }else{

                pDialog.dismiss();
                Toast.makeText(Main2Activity.this, " Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
