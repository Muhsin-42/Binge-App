package com.example.binge.Models;

public class MovieFirebase {
    String title,original_language,movie_overview,poster_path,release_date;
    int movie_id;
    float vote_average;

    //////////////////////////////
    ///     Constructor
    /////////////////////////////
    public MovieFirebase(String title, String original_language, String movie_overview, String poster_path, String release_date, int movie_id, float vote_average) {
        this.title = title;
        this.original_language = original_language;
        this.movie_overview = movie_overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.movie_id = movie_id;
        this.vote_average = vote_average;
    }

    public MovieFirebase() {
    }

    //////////////////////////
    //      Getter and Setter
    /////////////////////////
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getMovie_overview() {
        return movie_overview;
    }

    public void setMovie_overview(String movie_overview) {
        this.movie_overview = movie_overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }
}
