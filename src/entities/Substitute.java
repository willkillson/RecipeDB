package entities;

/**
 * A substitute of an ingredient. 
 * An ingredient can have many substitutes or none.
 */
public class Substitute {
    public String ingredientID;
    public String substitionName;

    public Substitute(String ingredientID, String substitionName) {
        this.ingredientID = ingredientID;
        this.substitionName = substitionName;
    }
}
