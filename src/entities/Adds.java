package entities;

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
}
