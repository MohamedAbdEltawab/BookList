package com.example.mohamed.booklist;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    // URL for search about book in googleapis
    public static  String BOOK_URL = "https://www.googleapis.com/books/v1/volumes?";

    public static String QUERY_PARAM = "q";


    public static List<Book> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = buildUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Book> books = extractFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return books;
    }

    /**
     *
     *  Return URL object from a given string url
     *
     */
    public static URL buildUrl(String query)
    {

        Uri builtUri = Uri.parse(BOOK_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, query).build();


        //String s = BOOK_URL + QUERY_PARAM + query;
        URL url = null;

        try {

            url = new URL(builtUri.toString());

            Log.w(TAG, "this is url ========> " +url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     *
     *  make http request to a given url and return a string as response
     *
     */
    public static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // check if url is null return early
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try
        {
            // make connection
           urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            }else{
                Log.e(TAG, "Error response code" + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     *
     *  this method to Convert the ( inputstream ) into a string which contain
     *  the whole json response from the server
     *
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null){

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    /**
     *
     *  Return a list of { Books } objects that from
     *  parsing the given JSON response.
     *
     */

    public static List<Book> extractFromJson(String bookJson){

        if (TextUtils.isEmpty(bookJson)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Book> books = new ArrayList<>();

        // Try to parse the JSON response string
        try {

            // Create a JsonObject from the json response string
            JSONObject baseJsonResponse = new JSONObject(bookJson);

            // Extract JsonArray associated with the key called "items"
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++){

                // Get a single book at position i within the list of books
                JSONObject currentBook = itemsArray.getJSONObject(i);

                // for a given book, extract the JSONObject
                // associated with a key called " volumeInfo "
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value For the key called " title "
                String bookName = volumeInfo.getString("title");

                // Get a JSONArray called " authors " For extract book author
                JSONArray authors = volumeInfo.getJSONArray("authors");

                // Extract the value For the key called " title "
                String bookAuthor = authors.getString(0);

                // Extract the value For the key called " title "
                //String description = volumeInfo.getString("description");

                // Extract the value to go to web page for key called " infoLink "
                String infoLink = volumeInfo.getString("infoLink");

                JSONObject imageLink = volumeInfo.getJSONObject("imageLinks");

                String bookImage = imageLink.getString("smallThumbnail");



                // for a given book , Extract JSONObject called " saledInfo "
                JSONObject saleInfo = currentBook.getJSONObject("saleInfo");



                Book book = new Book(bookName , bookAuthor, bookImage,/* description,*/
                        infoLink);

                books.add(book);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("NetworkUtils", "Problem parsing the earthquake JSON results", e);
        }

        return books;
    }

}
