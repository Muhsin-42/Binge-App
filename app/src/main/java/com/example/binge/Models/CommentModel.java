package com.example.binge.Models;

public class CommentModel {
    String commentId, commentImg, commentBy, commentMsg,movieId,uniqueKey;
    long commentAt;

    public CommentModel() {
    }

    public CommentModel(String commentId, String commentImg, String commentBy, String commentMsg, long commentAt,String movieId) {
        this.commentId = commentId;
        this.commentImg = commentImg;
        this.commentBy = commentBy;
        this.commentMsg = commentMsg;
        this.commentAt = commentAt;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentImg() {
        return commentImg;
    }

    public void setCommentImg(String commentImg) {
        this.commentImg = commentImg;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
    }

    public long getCommentAt() {
        return commentAt;
    }

    public void setCommentAt(long commentAt) {
        this.commentAt = commentAt;
    }
}
