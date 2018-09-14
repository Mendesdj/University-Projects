package pt.unl.fct.di.www.canicookit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;


/**
 * Created by ricardoesteves on 09/11/17.
 */

public class InfiniteFeedInfo {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("likes")
    @Expose
    private String likes;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("Difficulty")
    @Expose
    private String difficulty;

    @SerializedName("serves")
    @Expose
    private String serves;

    @SerializedName("fat")
    @Expose
    private String fat;

    @SerializedName("protein")
    @Expose
    private String protein;

    @SerializedName("calories")
    @Expose
    private String calories;

    @SerializedName("ingredients")
    @Expose
    private Ingredient [] ingredients;


    @SerializedName("steps")
    @Expose
    private String[] steps;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public String getFormattedTime () {
        return "Cooks in: " + time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDifficulty() {
        return "Difficulty: " + difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getServings() {
        return "Servings: " + serves;
    }

    public String getServes () {
        return serves;
    }

    public void setServings (String serves){this.serves = serves;}

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public String[] getSteps(){ return steps; }

    public String getFat() { return fat; }

    public String getProtein() { return protein; }

    public String getCalories() { return calories; }

    @Override
    public boolean equals (Object obj) {
        if (obj.getClass() != InfiniteFeedInfo.class)
            return false;
        InfiniteFeedInfo feed = (InfiniteFeedInfo) obj;

        return this.getTitle().equals(feed.getTitle());
    }
}


