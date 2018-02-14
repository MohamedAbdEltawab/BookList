package com.example.mohamed.booklist;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class MainFragment extends Fragment  implements BookAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<String>{

    private static final String TAG = NetworkUtils.class.getSimpleName();
    /**
     *  Interface reference for implement it in this fragment
     */
    private OnBookSelectedListener mCallbacks;
    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 22;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;
    private TextView mNoInternetMessage;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private TextView pleaseText;
    String jsonString ;
    private static final String QUERY = "query";

    SearchView searchView;

    Bundle queryBundle;

    TextView mShowJsonTextView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

            mCallbacks = (OnBookSelectedListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getSimpleName() + " Must implement OnBookSelectedListener");
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        progressBar = (ProgressBar)rootView.findViewById(R.id.pd_indicator);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.rc_recycler_view_item);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pleaseText = (TextView)rootView.findViewById(R.id.tv_pleae_search_a_book);
        mNoInternetMessage = (TextView)rootView.findViewById(R.id.tv_No_Internet_Connection);



        // This Decoration to classified each item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                new LinearLayoutManager(getActivity()).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);


        // This method called to menu search show in action bar
        setHasOptionsMenu(true);

        // Set Title of Main Fragment
        getActivity().setTitle(R.string.title_main_fragment);


        /**
         *  if saveInstanceState not null
         *  get json string from previous saveInstanceState that saved
         *  parse it and initialize adapter and make recyclerView set this adapter
         */
        if (savedInstanceState  != null){


            String qJson  = savedInstanceState.getString("jsonString");

            List<Book> books = NetworkUtils.extractFromJson(qJson);
            hideEmptyText();

            mAdapter = new BookAdapter(getContext(), books, this);
            mRecyclerView.setAdapter(mAdapter);


        }

        getLoaderManager().initLoader(BOOK_LOADER_ID, null, this);

        return rootView;
    }



    /**
     * This method to hide text( please, search a book ) and make recyclerview visible
     */
    private void hidePleaseText(){

        pleaseText.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }


    /**
     *  This Method make recyclerView is Gone and will appear text " No found books "
     */
    private void showEmptyText()
    {
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }


    /**
     *  This Method make recyclerView is Visible and will hide text " No found books "
     */
    private void hideEmptyText(){
        mEmptyStateTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * This method is check if internet is connect initialize the loader
     * if not internet access will appear text " Initialize the loader "
     */
    private void checkForInternet(){

        ConnectivityManager connMgr =(ConnectivityManager)
                getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            //LoaderManager loaderManager = getLoaderManager();


            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            LoaderManager loaderManager = getLoaderManager();
            Loader<String> bookSearchLoader = loaderManager.getLoader(BOOK_LOADER_ID);


            if (bookSearchLoader == null){

                loaderManager.initLoader(BOOK_LOADER_ID, queryBundle, this);

            }else {

                loaderManager.restartLoader(BOOK_LOADER_ID, queryBundle, this);

            }

            progressBar.setVisibility(View.VISIBLE);
            mNoInternetMessage.setVisibility(View.GONE);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
        //progressBar.setVisibility(View.INVISIBLE);
            getLoaderManager().destroyLoader(BOOK_LOADER_ID);
            mNoInternetMessage.setVisibility(View.VISIBLE);
            mNoInternetMessage.setText(R.string.NO_NET_CONNECTION);
        }

    }

    /**
     *  take query and append to url and call method checkForInternet
     */
    public void makeSearchQuery(String q) {

        queryBundle = new Bundle();

        queryBundle.putString(QUERY, q);

        checkForInternet();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toobar, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView)item.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String q) {

                makeSearchQuery(q);

                hidePleaseText();

                // Make keyboard hidden after query submit
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }


        });


        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     *  When click on item will implement mCallbacks interface
     */
    @Override
    public void onListItemClicked(Book book) {

        mCallbacks.onBookSelected(book);

    }


    /**
     *
     * To Update User interface after fetch data from internet
     */
    public void updateUi(String data){

        if (null == data) {
            showEmptyText();
        } else {

            hidePleaseText();

            List<Book> books = NetworkUtils.extractFromJson(data);
            hideEmptyText();

            mAdapter = new BookAdapter(getContext(), books, this);
            mRecyclerView.setAdapter(mAdapter);

        }

    }

    /**
     *
     *  This method will call first when ( LoaderManager.initLoader )  is execute
     */
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {


        return new AsyncTaskLoader<String>(getContext()) {


            String mBookJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();


                if (args == null){
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                if (mBookJson != null) {

                    deliverResult(mBookJson);

                } else {

                    forceLoad();

                }

            }

            @Override
            public String loadInBackground() {

                String queryString = args.getString(QUERY);

                URL quryURL = NetworkUtils.buildUrl(queryString);

                try {
                    return NetworkUtils.makeHttpRequest(quryURL);

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }


            }

            @Override
            public void deliverResult(String data) {
                mBookJson = data;
                super.deliverResult(mBookJson);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        progressBar.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")){

            jsonString = data;

            updateUi(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    /**
     *  Container Activity must implement this interface
     */
    public interface OnBookSelectedListener {
        void onBookSelected(Book book);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("jsonString", jsonString);
    }
}
