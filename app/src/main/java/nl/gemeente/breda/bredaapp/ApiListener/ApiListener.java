package nl.gemeente.breda.bredaapp.ApiListener;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by giede on 8-5-2017.
 */

public class ApiListener extends AsyncTask<String, Void, String>{

    private ApiListener listener = null;

    public ApiListener(ApiListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        String urlString = "https://asiointi.hel.fi/palautews/rest/v1/services.json";
        String respone = "";

        try {
            URL url = new URL(params[0]);
            URLConnection connection = new url.openConnection();

            String line;

            while ( (line = reader.readLine()) != null ) {
                respone += line;
            }

        } catch (MalformedURLException e) {
            Log.e("URLERROR", e.getLocalizedMessage());
        }



    }
}

/*


        } catch(MalformedURLException e) {
            Log.e("URLERROR", e.getLocalizedMessage());
            return null;
        } catch (IOException e){
            Log.e("URLERROR", e.getLocalizedMessage());
            return null;
        } catch (Exception e){
            Log.e("URLERROR", e.getLocalizedMessage());
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("URLERROR", e.getLocalizedMessage());
                    return null;
                }
            }
        }

        return response;

    }

    protected void onPostExecute(String response) {
        Log.i("POSTEXEC", response);



        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray products = jsonObject.getJSONArray("products");

            for (Integer i = 0; i < products.length(); i++) {

                // Parse JSON
                Product item = new Product();

                String title = products.getJSONObject(i).getString("title");
                String author = products.getJSONObject(i).getString("specsTag");
                String summary = products.getJSONObject(i).getString("summary");
                String shortDesc = products.getJSONObject(i).getString("shortDescription");
                String longDesc = products.getJSONObject(i).getString("longDescription");

                // Receiving images
                JSONArray image_urls = products.getJSONObject(i).getJSONArray("images");
                String smallImageUrl = image_urls.getJSONObject(0).getString("url");
                String largeImageUrl = image_urls.getJSONObject(4).getString("url");

                // Setting received values
                item.setTitle(title);
                item.setSpecsTag(author);
                item.setSummary(summary);
                item.setShortDescription(shortDesc);
                item.setLongDescription(longDesc);
                item.setSmallImageUrl(smallImageUrl);
                item.setLargeImageUrl(largeImageUrl);

                // Callback
                listener.onBolItemAvailable(item);



            }
        } catch(JSONException e) {
            Log.e("JSONEXEP", "Parse error");
        }

    }


    // Callback interface
    public interface BolListener {
        void onBolItemAvailable(Product product);
    }

}

 */