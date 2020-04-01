package com.example.problemsolver.Feed;

public class FeedProblem {

    private String date;
    private String street;
    private String rating;
    private String description;

    public FeedProblem(String date, String street, String rating, String description) {
        this.date = date;
        this.street = street;
        this.rating = rating;
        this.description = description;
    }
/*
    public int getStatusPic() {
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