package entities;

import java.util.ArrayList;
import java.util.Date;

public class Adds {

    String userID;
    public String recipeID;
    Date   lastCooked;
    Integer timesCooked;
    Double rating;

    public Adds(String userID, String recipeID, Date lastCooked, Integer timesCooked, Double rating){
        this.userID = userID;
        this.recipeID = recipeID;
        this.lastCooked = lastCooked;
        this.timesCooked = timesCooked;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return
                "ID: " + recipeID +  " LastCooked: " + lastCooked +
                " TimesCooked: " + timesCooked +
                " Rating: " + rating;
    }


    public static boolean contains(Recipe recipe, ArrayList<Adds> adds){
        boolean ret = false;

        for(int i = 0;i<adds.size();i++){
            if(recipe.getRecipeId().compareTo(adds.get(i).recipeID)==0){
                ret = true;
            }
        }

        return ret;
    }
}
