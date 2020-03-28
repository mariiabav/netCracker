package com.example.problemsolver.Feed;

public class FeedProblem {

    private int id;
    private int statusPic;
    private String date;
    private String street;
    private String rating;
    private String description;

    public FeedProblem(int id, int statusPic, String date, String street, String rating, String description) {
        this.setId(id);
        //this.statusPic = statusPic;
        this.date = date;
        this.street = street;
        this.rating = rating;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public int getStatusPic() {
        return statusPic;
    }

    public void setStatusPic(int statusPic) {
        this.statusPic = statusPic;
    }
    */


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}