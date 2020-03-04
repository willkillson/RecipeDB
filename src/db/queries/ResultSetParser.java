package db.queries;

import entities.Adds;
import entities.Ingredient;
import entities.Lists;
import entities.Recipe;
import entities.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Methods regarding parsing parts of the database to be used elsewhere.
 */
public class ResultSetParser {

    /**
     * Parses a list of LISTS relation.
     * 
     * @param result	to be parsed
     * @return the parsed list
     * @throws SQLException
     */
    public static ArrayList<Lists> parseLists(ResultSet result) throws SQLException {
        ArrayList<Lists> lists = new ArrayList<>();
        while (result.next()) {
            lists.add(parseList(result));
        }
        return lists;
    }


    /**
     * Parses the LISTS relation.
     * 
     * @param result	to be parsed
     * @return list
     * @throws SQLException
     */
    public static Lists parseList(ResultSet result) throws SQLException {

        String recipeID = null;
        String ingredientID = null;
        boolean isRequired = false;

        recipeID = result.getString("recipeID");
        ingredientID = result.getString("ingredientID");
        isRequired = result.getBoolean("isRequired");

        return new Lists(recipeID, ingredientID, isRequired);
    }


    /**
     * Parses a list of ingredients.
     * 
     * @param result	to be parsed
     * @return the parsed list
     * @throws SQLException
     */
    public static ArrayList<Ingredient> parseIngredients(ResultSet result) throws SQLException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        while (result.next()) {
            ingredients.add(parseIngredient(result));
        }
        return ingredients;
    }

    /**
     * Parses an ingredient.
     * 
     * @param result	to be parsed
     * @return ingredient
     * @throws SQLException
     */
    public static Ingredient parseIngredient(ResultSet result) throws SQLException {
        String ingredientID = result.getString("ingredientID");
        String name = result.getString("name");
        return new Ingredient(ingredientID, name);
    }

    /**
     * Parses a list of users.
     * 
     * @param result	to be parsed
     * @return the parsed list
     * @throws SQLException
     */
    public static ArrayList<User> parseUsers(ResultSet result) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (result.next()) {
            users.add(parseUser(result));
        }
        return users;
    }

    /**
     * Parses a user.
     * 
     * @param result	to be parsed
     * @return user
     * @throws SQLException
     */
    public static User parseUser(ResultSet result) throws SQLException {
        String userId = result.getString("userID");
        String email = result.getString("cupboardID");
        String cupboardID = result.getString("cupboardID");
        String cartID = result.getString("cartID");
        return new User(userId, email, cupboardID, cartID);
    }

    /**
     * Parses a list of recipes.
     * 
     * @param result	to be parsed
     * @return the parsed list
     * @throws SQLException
     */
    public static ArrayList<Recipe> parseRecipes(ResultSet result) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        while (result.next()) {
            recipes.add(parseRecipe(result));
        }
        return recipes;
    }

    /**
     * Parses a recipe.
     * 
     * @param result	to be parsed
     * @return recipe
     * @throws SQLException
     */
    public static Recipe parseRecipe(ResultSet result) throws SQLException {
        String recipeID = result.getString("recipeID");
        String name = result.getString("name");
        String url = result.getString("url");
        return new Recipe(recipeID, name, url);
    }

    /**
     * Parses a list of recipes with extra information.
     * 
     * @param result	to be parsed
     * @return the parsed list
     * @throws SQLException
     */
    public static ArrayList<Recipe> parseAugmentedRecipes(ResultSet result) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        while (result.next()) {
            recipes.add(parseAugmentedRecipe(result));
        }
        return recipes;
    }

    /**
     * Parses a recipe with extra information.
     * 
     * @param result	to be parsed
     * @return recipe
     * @throws SQLException
     */
    public static Recipe parseAugmentedRecipe(ResultSet result) throws SQLException {
        String recipeID = result.getString("recipeID");
        String name = result.getString("name");
        Double rating = result.getDouble("rating");
        String url = result.getString("url");
        int timesCooked = result.getInt("timesCooked");
        Date lastCooked = result.getDate("lastCooked");
        return new Recipe(recipeID, name, Optional.of(rating), url, timesCooked, lastCooked);
    }

    /**
     * Parses a list of the Adds relation.
     * 
     * @param result	to be parsed
     * @return	the parsed list
     * @throws SQLException
     */
    public static ArrayList<Adds> parseAddsList(ResultSet result) throws SQLException {
        ArrayList<Adds> adds = new ArrayList<>();
        while (result.next()) {
            adds.add(parseAdds(result));
        }
        return adds;
    }

    /**
     * Parses an Adds relation.
     * 
     * @param result	to be parsed
     * @return Adds relation
     * @throws SQLException
     */
    public static Adds parseAdds(ResultSet result) throws SQLException {
        String userId = result.getString("userID");
        String recipeID = result.getString("recipeID");
        Integer timesCooked = result.getInt("timesCooked");
        Date lastCooked = result.getDate("lastCooked");
        Double rating = result.getDouble("rating");
        return new Adds(userId, recipeID, lastCooked, timesCooked, rating);
    }
}
