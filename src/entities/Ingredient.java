package entities;

/**
 * An ingredient that exists in some recipe(s) in the database.
 */
public class Ingredient {
    private String ingredientId;
    private String name;

    public Ingredient(String ingredientId, String name) {
        this.ingredientId = ingredientId;
    	this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIngredientId() {
        return ingredientId;
    }
}
