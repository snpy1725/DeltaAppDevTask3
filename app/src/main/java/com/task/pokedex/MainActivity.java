package com.task.pokedex;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
public int i,j;
    public class doingwork extends AsyncTask<Void,Void,Void>{
        String h,m,k,l,s;
        String picurl;
        public JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

            HttpURLConnection urlConnection = null;

            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setDoOutput(true);

            urlConnection.connect();

            BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

            char[] buffer = new char[1024];

            String jsonString;

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();

            jsonString = sb.toString();



            return new JSONObject(jsonString);
        }@Override
        public void onPreExecute(){
            EditText t=(EditText)findViewById(R.id.pokesearch);
          s=t.getText().toString();
            ProgressBar x=(ProgressBar)findViewById(R.id.progressbar);
            x.setVisibility(View.VISIBLE);
        }


        @Override
        public Void doInBackground(Void... pa){



                try {
                    JSONObject jsonObject = getJSONObjectFromURL("http://pokeapi.co/api/v2/pokemon/" + s.toLowerCase());
                    String name = jsonObject.getString("name");
                    if ((name == null)||(name.equals(""))) {
                        h = "No Pokemon exists";

                        m = "None";
                        k = "None";
                        l = "None";
                    } else {

                        h = name;
                        m = jsonObject.getString("height");
                        k = jsonObject.getString("weight");


                        JSONObject piclink = jsonObject.getJSONObject("sprites");
                        picurl = piclink.getString("front_default");


                        JSONArray list = jsonObject.getJSONArray("types");
                        String overalltype = new String();
                        for (j = 0; j < list.length(); j++) {
                            JSONObject x = list.getJSONObject(j);
                            JSONObject typeobject = x.getJSONObject("type");
                            overalltype = overalltype + typeobject.getString("name") + ",";
                        }
                        l = overalltype;
                        SharedPreferences settings5 = getSharedPreferences("name5", 0);
                        SharedPreferences.Editor editor5 = settings5.edit();
                        editor5.putString("imageview", picurl);
                        editor5.apply();
                    }

                    } catch(IOException e){
                        e.printStackTrace();
                    } catch(JSONException e){
                        e.printStackTrace();
                    }


            return null;
        }
        @Override
        public void onPostExecute(Void pa){
            TextView nameview=(TextView)findViewById(R.id.pokename);
            nameview.setText(h);
            TextView heightview=(TextView)findViewById(R.id.pokeheight);
            heightview.setText(m);
            TextView weightview=(TextView)findViewById(R.id.pokeweight);
            weightview.setText(k);
            ImageView pic=(ImageView)findViewById(R.id.pokepic);
            Bitmap bmp = null;
            URL picurli= null;
            try {
                picurli = new URL(picurl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                if (picurli != null) {
                    bmp = BitmapFactory.decodeStream(picurli.openConnection().getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            pic.setImageBitmap(bmp);
            TextView types=(TextView)findViewById(R.id.poketype);
            types.setText(l);
            ProgressBar x=(ProgressBar)findViewById(R.id.progressbar);
            x.setVisibility(View.INVISIBLE);
            SharedPreferences settings1=getSharedPreferences("name1",0);
            SharedPreferences.Editor editor1=settings1.edit();


            editor1.putString("nameview",h);
            editor1.apply();
            SharedPreferences settings2=getSharedPreferences("name2",0);
            SharedPreferences.Editor editor2=settings2.edit();


            editor2.putString("heightview",m);
            editor2.apply();
            SharedPreferences settings3=getSharedPreferences("name3",0);
            SharedPreferences.Editor editor3=settings3.edit();


            editor3.putString("weightview",k);
            editor3.apply();
            SharedPreferences settings4=getSharedPreferences("name4",0);
            SharedPreferences.Editor editor4=settings4.edit();


            editor4.putString("typeview",l);
            editor4.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout x=(ConstraintLayout)findViewById(R.id.layout);
        x.setBackgroundColor(Color.rgb(239,102,96));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        SharedPreferences settings1=getSharedPreferences("name1",0);
        SharedPreferences settings2=getSharedPreferences("name2",0);
        SharedPreferences settings3=getSharedPreferences("name3",0);
        SharedPreferences settings4=getSharedPreferences("name4",0);
        SharedPreferences settings5=getSharedPreferences("name5",0);
       TextView t=(TextView)findViewById(R.id.pokename);
        TextView r=(TextView)findViewById(R.id.pokeheight);
        TextView s=(TextView)findViewById(R.id.pokeweight);
        TextView u=(TextView)findViewById(R.id.poketype);
        t.setText(settings1.getString("nameview",""));
        r.setText(settings2.getString("heightview",""));
        s.setText(settings3.getString("weightview",""));
        u.setText(settings4.getString("typeview",""));
        ImageView v=(ImageView)findViewById(R.id.pokepic);
        try {
            URL pic=new URL(settings5.getString("imageview",""));
            Bitmap bmp = BitmapFactory.decodeStream(pic.openConnection().getInputStream());
            v.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onStop()
    {
        super.onStop();
        SharedPreferences settings1=getSharedPreferences("name1",0);
        SharedPreferences.Editor editor1=settings1.edit();
        TextView x=(TextView)findViewById(R.id.pokename);

        editor1.putString("nameview",x.getText().toString());
        editor1.apply();
        SharedPreferences settings2=getSharedPreferences("name2",0);
        SharedPreferences.Editor editor2=settings2.edit();
        TextView y=(TextView)findViewById(R.id.pokeheight);

        editor2.putString("heightview",y.getText().toString());
        editor2.apply();
        SharedPreferences settings3=getSharedPreferences("name3",0);
        SharedPreferences.Editor editor3=settings3.edit();
        TextView z=(TextView)findViewById(R.id.pokeweight);

        editor3.putString("weightview",z.getText().toString());
        editor3.apply();
        SharedPreferences settings4=getSharedPreferences("name4",0);
        SharedPreferences.Editor editor4=settings4.edit();
        TextView a=(TextView)findViewById(R.id.poketype);

        editor4.putString("typeview",a.getText().toString());
        editor4.apply();


    }



    public void searchpress(View view){
      doingwork x=new doingwork();
        x.execute();
    }
}
