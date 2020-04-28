package com.example.problemsolver.problemFeed.model;

import com.example.problemsolver.event.Model.Problem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("person")
    @Expose
    private Person person;

    @SerializedName("problem")
    @Expose
    private Problem problem;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("creationDate")
    @Expose
    private String CreationDate;

    public Comment() {
    }

    public Comment(Person person, Problem problem, String text) {
        this.person = person;
        this.problem = problem;
        this.text = text;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }
}
