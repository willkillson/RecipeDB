package entities;

/**
 * LISTS relation from the database.
 * LISTS is connecting ingredients to recipes.
 */
public class Lists {
    public String recipeID;
    public String ingredientID;
    public boolean isRequired;

    /**
     * Lists method
     * @param recipeID
     * @param ingredientID
     * @param isRequired
     */
    public Lists(String recipeID, String ingredientID, boolean isRequired) {
        this.ingredientID = ingredientID;
        this.recipeID = recipeID;
        this.isRequired = isRequired;
    }
}
