package com.example.mohamed.booklist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder>{

    private static final String TAG = BookAdapter.class.getSimpleName();

    final private ListItemClickListener mOnClickListener;

    private List<Book> mBooks;

    Context context;

    /** this interface for get position of item that clicked */
    public interface ListItemClickListener{

        void onListItemClicked(Book book);
    }



    public BookAdapter(Context context, List<Book> mBooks, ListItemClickListener listener){
        this.context = context;
        this.mBooks = mBooks;
        mOnClickListener = listener;

    }

    public  class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Define Book Name TextView
        TextView bookNameTextView;
        // Define Book Author TextView;
        TextView authorTextView;
        // Define Book imageView
        ImageView bookImage;


        public BookViewHolder(View itemView) {
            super(itemView);
            bookNameTextView = (TextView)itemView.findViewById(R.id.tv_book_name);
            authorTextView = (TextView)itemView.findViewById(R.id.tv_book_author);
            bookImage = (ImageView)itemView.findViewById(R.id.iv_bookImage);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClicked(mBooks.get(clickedPosition));
        }
    }

    /* Create new views  ( invoked by layout manager ) */

    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_list_item, parent, false);

        BookViewHolder viewHolder = new BookViewHolder(view);

        return viewHolder;
    }

    /**
     *  This method bind or populate data to RecyclerView
     */
    @Override
    public void onBindViewHolder(BookAdapter.BookViewHolder holder, int position) {

        holder.bookNameTextView.setText(mBooks.get(position).getBookName());
        holder.authorTextView.setText(mBooks.get(position).getBookAutor());


        //Render image using Picasso library
        if (!TextUtils.isEmpty(mBooks.get(position).getBookImage())) {
            Picasso.with(context)
                    .load(mBooks.get(position).getBookImage())
                    .into(holder.bookImage);


        }
    }

    /**
     *
     * @return ( int ) number of items of recyclerview
     */
    @Override
    public int getItemCount() {
        return (null != mBooks ? mBooks.size() : 0);
    }
}
