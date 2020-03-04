package entities;

/**
 * An ingredient that exists in some recipe(s) in the database.
 */
public class Ingredient {
    private String ingredientId;
    private String name;

    /**
     * Ingredient method
     * @param ingredientId
     * @param name
     */
    public Ingredient(String ingredientId, String name) {
        this.ingredientId = ingredientId;
        this.name = name;
    }

    /**
     * getter for name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * getter for IngredientID
     * @return String
     */
    public String getIngredientId() {
        return ingredientId;
    }

    /**
     * comparison method for Ingredients
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

        Ingredient that = (Ingredient) o;

        if (ingredientId != null ? !ingredientId.equals(that.ingredientId) : that.ingredientId != null) {
            return false;
        }
        return name != null ? name.equals(that.name) : that.name == null;
    }

    /**
     * Hashcode to hash Ingredients
     * @return int
     */
    @Override
    public int hashCode() {
        int result = ingredientId != null ? ingredientId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    /**
     * to String method for Ingredients
     * @return String
     */
    @Override
    public String toString() {
        return "Name: " + name + " Id: " + ingredientId;
    }
}
