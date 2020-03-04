package entities;

/**
 * Substitute entity to store ingredient substitutes from the mySQL database
 */
public class Substitute {
    public String ingredientID;
    public String substitionName;

    /**
     * Substitute method
     * @param ingredientID
     * @param substitionName
     */
    public Substitute(String ingredientID, String substitionName) {
        this.ingredientID = ingredientID;
        this.substitionName = substitionName;
    }
}
