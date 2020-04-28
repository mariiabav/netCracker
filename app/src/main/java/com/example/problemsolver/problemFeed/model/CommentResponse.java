package com.example.problemsolver.problemFeed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentResponse {
    @SerializedName("commentDTOList")
    @Expose
    private List<Comment> commentList;

    //@SerializedName("pagesLimit")
    //@Expose
    //private Integer pagesLimit;


    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
