package db.queries;

import db.ServerDB;
import entities.Ingredient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.Result;

/**
 * Queries regarding the ingredients in general in the database.
 * (i.e. not just those in a specific cart, cupboard, or recipe list.)
 */
public class IngredientQueries {

    /**
     * Retrieves all ingredients in the database.
     * 
     * @param server 	database
     * @return All ingredients or an error
     */
    public static Result<ArrayList<entities.Ingredient>> getIngredients(ServerDB server) {
    	// set up connection
    	Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
        	// create query to get all ingredients
            stat = conn.prepareStatement("SELECT ingredientID, name "
                + "FROM INGREDIENT");

            // execute query
            result = stat.executeQuery();
            
            // build result list
            ArrayList<Ingredient> ingredients = ResultSetParser.parseIngredients(result);
            return Result.success(ingredients);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Result.failure("There was an error processing your request. " +
            "Please contact software developer with the previous output");
    }
}
