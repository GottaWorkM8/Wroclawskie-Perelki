package pl.szajsjem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class JsonTest {
    public static void main(String[] args) throws ClientProtocolException, IOException, JSONException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://szajsjem.mooo.com/api/zagadka");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        JSONObject jo = new JSONObject("{\"d\":"+rd.readLine()+"}");
        var arr = jo.getJSONArray("d");
        for(int i=0;i<arr.length();i++){
            JSONObject zagadka = ((JSONObject)arr.get(i));
            int id = zagadka.getInt("id");
            String nazwa = zagadka.getString("name");
            System.out.println(id+" "+nazwa);
        }
    }
}
