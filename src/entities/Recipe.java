package entities;

import java.util.Date;
import java.util.Optional;

public class Recipe {
    private String recipeId;
    private String name;
    private Optional<Double> rating;
    private String url;
    private int timesCooked;
    private Date lastCooked;

    public Recipe(String recipeId, String name, Optional<Double> rating, String url, int timesCooked, Date lastCooked) {
        this.recipeId = recipeId;
        this.name = name;
        this.rating = rating;
        this.url = url;
        this.timesCooked = timesCooked;
        this.lastCooked = lastCooked;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public Optional<Double> getRating() { return rating; }

    public void setRating(double rating) { this.rating = Optional.of(rating); }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getTimesCooked() {
        return timesCooked;
    }

    public Date getLastCooked() {
        return lastCooked;
    }
}
