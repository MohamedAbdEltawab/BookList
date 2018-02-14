package com.example.mohamed.booklist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainFragment.OnBookSelectedListener,
        FragmentManager.OnBackStackChangedListener {

    Book mBook;
    private static final String TAG = MainActivity.class.getSimpleName();
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {

            startMainFragment();

         }

        //Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();

    }

    // start ( MainFragment )
    private void startMainFragment(){
        // This MainFragment will Create after MainActivity Created
        MainFragment mainFragment = new MainFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fl_main_frame, mainFragment)
                .commit();
    }


    /**
     *
     *  this method implement from interface " MainFragment.OnBookSelectedListener "
     *  to go to item detail in detailsFragment
     *
     */
    @Override
    public void onBookSelected(Book book) {

        startDetailsFragment(book);

    }



    private void startDetailsFragment(Book book){
        // This DetailsFragment Will Create when click on book item
        DetailsFragment detailsFragment  = new DetailsFragment(book);
        fragmentManager.beginTransaction()
                .replace(R.id.fl_main_frame, detailsFragment)
                .addToBackStack("detail")
                .commit();
    }


    /** on backStack changed  implement  " shouldDisplayHomeUp " */
    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    /** enable button back home to appear and active */
    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    /** return true if backStack from fragment */
    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
