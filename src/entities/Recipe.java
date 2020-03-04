package entities;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;


/**
 * Entity object for Recipes that are pulled from the mySQL database
 */
public class Recipe {
    private String recipeId;
    private String name;
    private Optional<Double> rating;
    private String url;
    private int timesCooked;
    private Date lastCooked;

    /**
     * recipe method
     * @param recipeId
     * @param name
     * @param rating
     * @param url
     * @param timesCooked
     * @param lastCooked
     */
    public Recipe(String recipeId, String name, Optional<Double> rating,
                  String url, int timesCooked, Date lastCooked) {
        this.recipeId = recipeId;
        this.name = name;
        this.rating = rating;
        this.url = url;
        this.timesCooked = timesCooked;
        this.lastCooked = lastCooked;
    }

    /**
     * recipe method for global recipe
     * @param recipeID
     * @param name
     * @param URL
     */
    public Recipe(String recipeID, String name, String URL) {
        this.recipeId = recipeID;
        this.name = name;
        this.url = URL;
        this.rating = null;
        this.timesCooked = 0;
        this.lastCooked = null;
    }

    /**
     * getter for recipe ID
     * @return
     */
    public String getRecipeId() {
        return recipeId;
    }

    /**
     * getter for name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getter for rating
     * @return rating
     */
    public Optional<Double> getRating() {
        return rating;
    }

    /**
     * setter for rating
     * @param rating
     */
    public void setRating(double rating) {
        this.rating = Optional.of(rating);
    }

    /**
     * getter for URL
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * recipe to string
     * @return String
     */
    @Override
    public String toString() {
        return "Name: " + getName() + " Id: " + getRecipeId() + " Url: " + getUrl();
    }

    /**
     * getter for times cooked
     * @return int
     */
    public int getTimesCooked() {
        return timesCooked;
    }

    /**
     * getter for lastcooked
     * @return Date
     */
    public Date getLastCooked() {
        return lastCooked;
    }


    /**
     * equals to compare recipe
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeId, recipe.recipeId);
    }

    /**
     * returns hashcode for recipe ID
     * @return int
     */
    @Override
    public int hashCode() {

        return Objects.hash(recipeId);
    }
}
