package pl.szajsjem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class JsonTest2 {
    public static void main(String[] args) throws ClientProtocolException, IOException, JSONException {

        int idmiejsca = 2;

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://szajsjem.mooo.com/api/miejsca/"+idmiejsca);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        JSONObject jo = new JSONObject(rd.readLine());

        jo.put("photoLink","Link do zdjęcia");
        jo.put("photoPosition","Lattitude"+", "+"Longtitude");//czy na odwrót bo nie wiem
        //i inne rzeczy tylko zeby nazwy byly dobre

        HttpPut res = new HttpPut("https://szajsjem.mooo.com/api/miejsca/"+idmiejsca);
        res.addHeader("content-type","application/JSON");
        res.setEntity(new StringEntity(jo.toString()));
        response = client.execute(res);

        rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(rd.readLine());
    }
}
