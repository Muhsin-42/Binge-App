package com.example.binge.Models;

public class NotificationModel {
    private String notifId,notifBy,notifType,commentId,commentBy,movieId,replyBy,replyId;
    private long notifAt;
    private boolean checkOpen;

    public NotificationModel() {
    }

    public NotificationModel(String notifId, String notifBy, String notifType, String commentId, String commentBy, String movieId, long notifAt, boolean checkOpen) {
        this.notifId = notifId;
        this.notifBy = notifBy;
        this.notifType = notifType;
        this.commentId = commentId;
        this.commentBy = commentBy;
        this.movieId = movieId;
        this.notifAt = notifAt;
        this.checkOpen = checkOpen;
    }

    public NotificationModel(String notifId, String notifBy, String notifType, String commentId, String commentBy, long notifAt, boolean checkOpen) {
        this.notifId = notifId;
        this.notifBy = notifBy;
        this.notifType = notifType;
        this.commentId = commentId;
        this.commentBy = commentBy;
        this.notifAt = notifAt;
        this.checkOpen = checkOpen;
    }

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getNotifId() {
        return notifId;
    }

    public void setNotifId(String notifId) {
        this.notifId = notifId;
    }

    public String getNotifType() {
        return notifType;
    }

    public void setNotifType(String notifType) {
        this.notifType = notifType;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getNotifBy() {
        return notifBy;
    }

    public void setNotifBy(String notifBy) {
        this.notifBy = notifBy;
    }

    public long getNotifAt() {
        return notifAt;
    }

    public void setNotifAt(long notifAt) {
        this.notifAt = notifAt;
    }

    public boolean isCheckOpen() {
        return checkOpen;
    }

    public void setCheckOpen(boolean checkOpen) {
        this.checkOpen = checkOpen;
    }
}
