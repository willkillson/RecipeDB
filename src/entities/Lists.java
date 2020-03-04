package entities;

/**
 * Lists entity object to store mySQL data for lists
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
