package entities;

import java.util.Optional;

public class Recipe {
    private String recipeId;
    private String name;
    private Optional<Double> rating;
    private String url;

    public Recipe(String recipeId, String name, Optional<Double> rating, String url) {
        this.recipeId = recipeId;
        this.name = name;
        this.rating = rating;
        this.url = url;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public Optional<Double> getRating() {
        return rating;
    }

    public String getUrl() {
        return url;
    }
}
