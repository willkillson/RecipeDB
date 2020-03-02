package db.queries;

import entities.Ingredient;
import entities.Lists;
import entities.Recipe;
import entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class ResultSetParser {

   public static ArrayList<Lists> parseLists(ResultSet result) throws SQLException {
        ArrayList<Lists> lists = new ArrayList<>();
        while (result.next()) {
            lists.add(parseList(result));
        }
        return lists;
    }

    public static Lists parseList(ResultSet result) throws SQLException {

        String recipeID = null;
        String ingredientID = null;
        boolean isRequired = false;

        recipeID = result.getString("recipeID");
        ingredientID = result.getString("ingredientID");
        isRequired = result.getBoolean("isRequired");

        return new Lists(recipeID,ingredientID,isRequired);
    }

    public static ArrayList<Ingredient> parseIngredients(ResultSet result) throws SQLException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        while (result.next()) {
            ingredients.add(parseIngredient(result));
        }
        return ingredients;
    }
    public static Ingredient parseIngredient(ResultSet result) throws SQLException {
        String ingredientID = result.getString("ingredientID");
        String name = result.getString("name");
        return new Ingredient(ingredientID, name);
    }

    public static ArrayList<User> parseUsers(ResultSet result) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (result.next()) {
            users.add(parseUser(result));
        }
        return users;
    }
    public static User parseUser(ResultSet result) throws SQLException {
        String userId = result.getString("userID");
        String email = result.getString("cupboardID");
        String cupboardID = result.getString("cupboardID");
        String cartID = result.getString("cartID");
        return new User(userId, email, cupboardID, cartID);
    }

    public static ArrayList<Recipe> parseRecipes(ResultSet result) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        while (result.next()) {
            recipes.add(parseRecipe(result));
        }
        return recipes;
    }

    public static Recipe parseRecipe(ResultSet result) throws SQLException {
        String recipeID = result.getString("recipeID");
        String name = result.getString("name");
        Double rating = result.getDouble("rating");
        String url = result.getString("url");
        int timesCooked = result.getInt("timesCooked");
        Date lastCooked = result.getDate("lastCooked");
        return new Recipe(recipeID, name, Optional.of(rating), url, timesCooked, lastCooked);
    }
}
