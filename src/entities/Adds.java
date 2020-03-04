package entities;

import java.util.ArrayList;
import java.util.Date;

/**
 * Entity where ADDS from the SQL database are stored
 */
public class Adds {

    String userID;
    public String recipeID;
    Date lastCooked;
    Integer timesCooked;
    Double rating;

    /**
     * Adds method
     * @param userID
     * @param recipeID
     * @param lastCooked
     * @param timesCooked
     * @param rating
     */
    public Adds(String userID, String recipeID, Date lastCooked, Integer timesCooked, Double rating) {
        this.userID = userID;
        this.recipeID = recipeID;
        this.lastCooked = lastCooked;
        this.timesCooked = timesCooked;
        this.rating = rating;
    }

    /**
     * prings an ADDs
     * @return
     */
    @Override
    public String toString() {
        return
            "ID: " + recipeID + " LastCooked: " + lastCooked +
                " TimesCooked: " + timesCooked +
                " Rating: " + rating;
    }

    /**
     * checks if ADDs contains an recipe
     * @param recipe
     * @param adds
     * @return
     */
    public static boolean contains(Recipe recipe, ArrayList<Adds> adds) {
        boolean ret = false;

        for (int i = 0; i < adds.size(); i++) {
            if (recipe.getRecipeId().compareTo(adds.get(i).recipeID) == 0) {
                ret = true;
            }
        }

        return ret;
    }
}
