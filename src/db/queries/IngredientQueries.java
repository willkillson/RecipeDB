package db.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.ServerDB;
import entities.Ingredient;
import entities.User;
import util.Result;

/**
 * Queries regarding the ingredients in general in the database.
 * (i.e. not just those in a specific cart, cupboard, or recipe list.)
 */
public class IngredientQueries {

	/**
	 * Retrieves all ingredients in the database.
	 */
	public static Result<ArrayList<entities.Ingredient>> getIngredients(ServerDB server) {
		Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
        	stat = conn.prepareStatement("SELECT ingredientID, name "
        			+ "FROM INGREDIENT");
        	
        	result = stat.executeQuery();
        	
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
