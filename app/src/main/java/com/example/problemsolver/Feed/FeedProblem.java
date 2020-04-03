package com.example.problemsolver.Feed;

import java.util.Comparator;

public class FeedProblem implements Comparable<FeedProblem>{

    //private int statusPic;
    private String date;
    private String street;
    private String rating;
    private String description;

    public FeedProblem(String date, String street, String rating, String description) {
        //this.statusPic = statusPic;
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


    @Override
    public int compareTo(FeedProblem problem) {
        String date = problem.getDate();
        //<0, если аргумент большая строка
        return date.compareTo(this.date);
    }

    public static Comparator<FeedProblem> FeedProblemRatingComparator = new Comparator<FeedProblem >() {
        public int compare(FeedProblem problem1, FeedProblem problem2) {

            String problemRating1 = problem1.getRating();
            String problemRating2 = problem2.getRating();

            return problemRating2.compareTo(problemRating1);
        }
    };
}