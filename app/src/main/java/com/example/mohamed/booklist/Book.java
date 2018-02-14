package com.example.mohamed.booklist;


public class Book {

    private String bookName;

    private String bookAutor;

    private String bookImage;

    //private String description;


    private String webPageLink;

    public Book( String name, String author, String bookImage, /*String description,*/
                String webPageLink){

        this.bookName = name;
        this.bookAutor = author;
        this.bookImage = bookImage;
        //this.description = description;

        this.webPageLink = webPageLink;

    }

    // This method for get Book Name
    public String getBookName() {
        return bookName;
    }

    // This method for get Book Author
    public String getBookAutor() {
        return bookAutor;
    }

    // this method for get Book Image
    public String getBookImage(){
        return bookImage;
    }

    // This method for get book description
//    public String getDescription() {
//        return description;
//    }


    // this method for get link web page for book
    public String getWebPageLink() {
        return webPageLink;
    }
}
