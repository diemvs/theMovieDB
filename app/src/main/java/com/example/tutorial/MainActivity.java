package com.example.tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private ArrayList<Movie> movieList;
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    @Override
    public void processFinish(ArrayList<Movie> input){
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> desc = new ArrayList<>();
        for(int i =0; i < input.size(); i++){
            titles.add(input.get(i).getTitle());
            desc.add(input.get(i).getDesc());
        }

        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, titles);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Description:");
                dialog.setContentView(R.layout.dialog_view);
                TextView text = (TextView) dialog.findViewById(R.id.dialogTextView);
                text.setText(desc.get(i));
                dialog.show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();

        MyTask mt = new MyTask();
        mt.delegate = (AsyncResponse) this;
        mt.execute();

    }
    private class MyTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        public AsyncResponse delegate = null;

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/popular?api_key=9e952c5dc8826d45b1a11e12f1b77519");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setDoInput(true);
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");

                InputStream in = con.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);

                BufferedReader buff = new BufferedReader(isw);

                StringBuilder responseStrBuilder = new StringBuilder();
                System.out.println("Output from Server #2 .... \n");
                String inputStr;
                while ((inputStr = buff.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                jsonObject = new JSONObject(responseStrBuilder.toString());
                jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    String title = jsonObj.getString("original_title");
                    String desc = jsonObj.getString("overview");
                    System.out.println("MOVIE LIST: " + title);
                    Movie model = new Movie();

                    model.setId(i);
                    model.setDesc(desc);
                    model.setTitle(title);

                    movieList.add(model);
                }

            } catch (IOException | JSONException e){
                e.printStackTrace();
                System.out.println(e.toString());

            }
            return movieList;
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> res) {
            delegate.processFinish(res);
        }
    }
}

