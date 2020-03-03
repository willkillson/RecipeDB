package entities;

import java.util.Date;
import java.util.Objects;
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

    public Recipe(String recipeID, String name, String URL){
        this.recipeId = recipeID;
        this.name = name;
        this.url = URL;
        this.rating = null;
        this.timesCooked = 0;
        this.lastCooked = null;
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
        return "Name: " + getName()+" Id: "+getRecipeId()+" Url: "+getUrl();
    }

    public int getTimesCooked() {
        return timesCooked;
    }

    public Date getLastCooked() {
        return lastCooked;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeId, recipe.recipeId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(recipeId);
    }
}
