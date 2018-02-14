package com.example.mohamed.booklist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


@SuppressLint("ValidFragment")
public class DetailsFragment extends Fragment{

    Book mBook;

    public DetailsFragment(){

    }
    @SuppressLint("ValidFragment")
    public DetailsFragment(Book book){
        this.mBook = book;
    }
    ImageView bookImageView;
    TextView bookNameTextView;
    TextView bookAuthorTextView;
    TextView bookDescriptionTextView;
    Button webButton;


    String bookName ;
    String bookAuthor;
    String bookImage ;
    String bookWebPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        bookImageView = (ImageView)rootView.findViewById(R.id.iv_details_book_image);
        bookNameTextView = (TextView)rootView.findViewById(R.id.tv_details_book_name);
        bookAuthorTextView = (TextView)rootView.findViewById(R.id.tv_details_book_author);
        bookDescriptionTextView = (TextView)rootView.findViewById(R.id.tv_details_book_description);
        webButton = (Button)rootView.findViewById(R.id.btn_go_to_web);

        // for show menu in actionbar
        setHasOptionsMenu(true);

        if (savedInstanceState == null){

            bookName = mBook.getBookName();
            bookAuthor = mBook.getBookAutor();
            bookImage = mBook.getBookImage();
            bookWebPage = mBook.getWebPageLink();

        }else {

            bookName = savedInstanceState.getString("bookName");
            bookAuthor = savedInstanceState.getString("bookAuthor");
            bookImage = savedInstanceState.getString("bookImage");
            bookWebPage = savedInstanceState.getString("bookWebPage");

        }

        // Set Title of Details Fragment
        getActivity().setTitle(bookName);

        return rootView;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Picasso.with(getContext()).load(bookImage).into(bookImageView);

        bookNameTextView.setText(bookName);

        bookAuthorTextView.setText(bookAuthor);

        //bookDescription.setText(mBook.getDescription());
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Uri webPage = Uri.parse(bookWebPage);
            Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }

            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("bookName", bookName);
        outState.putString("bookAuthor",bookAuthor);
        outState.putString("bookImage", bookImage);
        outState.putString("bookWebPage", bookWebPage);
    }

}
