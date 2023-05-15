package com.example.binge.Models;

public class ReplyModel {

    String replyId, commentId, replyBy, replyMsg;
    long replyAt;

    public ReplyModel() {
    }

    public ReplyModel(String replyId, String commentId, String replyBy, String replyMsg, long replyAt) {
        this.replyId = replyId;
        this.commentId = commentId;
        this.replyBy = replyBy;
        this.replyMsg = replyMsg;
        this.replyAt = replyAt;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReplyBy() {
        return replyBy;
    }

    public void setReplyBy(String replyBy) {
        this.replyBy = replyBy;
    }

    public String getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
    }

    public long getReplyAt() {
        return replyAt;
    }

    public void setReplyAt(long replyAt) {
        this.replyAt = replyAt;
    }
}
