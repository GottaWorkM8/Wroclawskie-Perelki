package wro.per.fragments;


import android.graphics.Color;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import wro.per.R;
import wro.per.others.RecyclerViewAdapter;
import wro.per.others.Zagadki;


public class FragmentWTrakcie extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        class FetchData extends Thread {

            private List<Zagadki> zagadkiList = new ArrayList<>();

            @Override
            public void run() {
                try {
                    URL url = new URL("https://szajsjem.mooo.com/api/zagadka");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    StringBuilder data = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        data.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(data.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Zagadki zagadka = new Zagadki();
                        zagadka.setId(jsonObject.getInt("id"));
                        zagadka.setDifficulty(jsonObject.getString("difficulty"));
                        zagadka.setName(jsonObject.getString("name"));
                        zagadka.setObjectCount(jsonObject.isNull("objectCount") ? null : jsonObject.getInt("objectCount"));
                        zagadka.setInfoLink(jsonObject.getString("infolink"));
                        zagadka.setAuthor(jsonObject.getString("author"));
                        zagadka.setPoints(jsonObject.isNull("points") ? null : jsonObject.getInt("points"));
                        zagadkiList.add(zagadka);
                    }
                    bufferedReader.close();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        FetchData fetchData = new FetchData();
        fetchData.start(); // uruchamia wątek i wywołuje metodę run()

        while (fetchData.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<Zagadki> zagadkiList = fetchData.zagadkiList;


        View view = inflater.inflate(R.layout.fragment_w_trakcie, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(zagadkiList, view.getContext()));


        return view;

        // Inflate the layout for this fragment

    }
}