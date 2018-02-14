package com.example.mohamed.booklist;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;


/**
 *  Loads a list of books by using an AsyncTask to perform the
 *  network request to the given URL.
 */
public class BookLoader extends AsyncTaskLoader<List<Book>> {


    private static final String TAG = BookLoader.class.getSimpleName();

    private String query;

    public BookLoader(Context context, String query) {
        super(context);
        query = query;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {

        if (query == null){
            return null;
        }

        List<Book> books = NetworkUtils.fetchEarthquakeData(query);

        return books;

    }


}
