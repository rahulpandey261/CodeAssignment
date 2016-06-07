package romeo.com.quicktest;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RequestData implements Runnable{

    private String url;
    private String response = null;
    private DataListener dl;
    private List<ImageDetails> imageDetailses;

    public RequestData(DataListener dl, String url){
        imageDetailses = new ArrayList<>();
        this.url = url;
        this.dl = dl;
    }

    @Override
    public void run() {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            int responseCode = con.getResponseCode();
            Log.d("GET Response Code :: ",  String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //dl.Success(response.toString());
                this.response = response.toString();
                Log.d("ILUD pages", this.response);
            } else {
                dl.onFailure("Network Error");
            }
        }catch (Exception e){
            dl.onFailure("Network or URL Error");
        }
        if(response!=null) {
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("query").getJSONObject("pages");

                Iterator<String> keys = jsonObject.keys();
                do{
                    String key = keys.next();
                    JSONObject innerObject = jsonObject.getJSONObject(key);
                    String name = innerObject.getString("title");
                    String imageUrl = null;
                    int height = -1;
                    int width  = -1;
                    if(innerObject.has("thumbnail"))
                    {
                        JSONObject thumbNailObject = innerObject.getJSONObject("thumbnail");
                        imageUrl = thumbNailObject.getString("source");
                        width = thumbNailObject.getInt("width");
                        height = thumbNailObject.getInt("height");
                    }
                    imageDetailses.add(new ImageDetails(name, imageUrl, height, width));

                }while (keys.hasNext());

                dl.onSuccess(imageDetailses);
            }catch (Exception e) {
                dl.onFailure("Error in Json Data");
            }

        }
        else {
            dl.onFailure("Error in Network connection");
        }

    }



    public interface DataListener {

        public void onSuccess(List<ImageDetails> imageDetailses);
        public void onFailure(String response);

    }
}
