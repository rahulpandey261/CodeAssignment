package romeo.com.quicktest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static List<ImageDetails> mImageDetails = new ArrayList<>();
    private RecyclerView mImageList;

    private EditText mEtSearch;
    private ImageButton mIbSearch;

    private ProgressDialog mProgressDialog = null;
    private  AppAdapter mAdapter;

    private static String JSON_URL="https://en.wikipedia.org/w/api.php?%20action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=150&%20pilimit=50&generator=prefixsearch&gpssearch=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mImageList  = (RecyclerView) findViewById(R.id.rv_image_list);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mIbSearch = (ImageButton) findViewById(R.id.ib_search);


        mIbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = ProgressDialog.show(MainActivity.this, "Loading...", "Please Wait While Loading...");
                String searchQuery = mEtSearch.getText().toString();
                if(searchQuery!=null && !searchQuery.isEmpty()) {

                    View view = MainActivity.this.getCurrentFocus();
                    if(v!=null){
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromInputMethod(mEtSearch.getWindowToken(),0);
                    }
                    new RequestString().execute(JSON_URL+searchQuery);
                }
            }
        });

        //mImageList.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));



    }


    private class RequestString extends AsyncTask<String, String, List<ImageDetails>>{

        String response = null;
        List<ImageDetails> imageDetailses = new ArrayList<>();

        @Override
        protected List<ImageDetails> doInBackground(String[] params) {

            try {
                URL urlObj = new URL(params[0]);
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
                    //dl.onFailure("Network Error");
                }
            }catch (Exception e){
                //dl.onFailure("Network or URL Error");
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

                    //dl.onSuccess(imageDetailses);
                }catch (Exception e) {
                    //dl.onFailure("Error in Json Data");
                }

            }
            else {
                //dl.onFailure("Error in Network connection");
            }

            return imageDetailses;
        }
        @Override
        protected void onPostExecute(List<ImageDetails> result){
            mImageDetails = result;
            mAdapter = new AppAdapter(mImageDetails);
            mImageList.setAdapter(mAdapter);
            mImageList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mAdapter.notifyDataSetChanged();
            Log.d("ILUD MainActivity:", "Custom Adapter is set");
            if(mProgressDialog!=null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                Log.d("MainActivity:", String.valueOf(mAdapter.getItemCount()));
            }
        }
    }

}
