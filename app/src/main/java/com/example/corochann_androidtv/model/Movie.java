package com.example.corochann_androidtv.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by alan on 30/08/2017.
 */

public class Movie implements Serializable{
    private static final String TAG = Movie.class.getSimpleName();

    static final long serialVersionUID = 727566175075960653L;
    private long id;
    private String title;
    private String studio;
    private String cardImageUrl;
    private String description;
    private String videoUrl;

    public Movie(){}

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public URI getCardImageURI(){
        try{
            return new URI(getCardImageUrl());
        } catch (URISyntaxException e){
            return null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
