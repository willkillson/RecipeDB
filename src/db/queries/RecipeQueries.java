package db.queries;

import db.ServerDB;
import entities.Recipe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.text.html.Option;
import util.Result;

public class RecipeQueries {
    public static Result<ArrayList<entities.Recipe>> getRecipes(
        ServerDB server, int start, int size) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT RECIPE.recipeID as recipeID, RECIPE.name as name,  " +
                    "RECIPE.URL as url, Avg(ADDS.rating) as rating FROM RECIPE, ADDS " +
                    "WHERE ADDS.recipeID=RECIPE.recipeID GROUP BY RECIPE.recipeID LIMIT ?, ?;");

            stat.setInt(1, start);
            stat.setInt(2, size);

            result = stat.executeQuery();

            ArrayList<Recipe> recipes = new ArrayList<>();
            while (result.next()) {
                String recipeID = result.getString("recipeID");
                String name = result.getString("name");
                Double rating = result.getDouble("rating");
                String url = result.getString("url");
                recipes.add(new Recipe(recipeID, name, Optional.of(rating), url));
            }
            return Result.success(recipes);
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
