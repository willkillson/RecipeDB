package db.queries;

import db.ServerDB;
import entities.Adds;
import entities.Lists;
import entities.Recipe;
import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import util.Result;

public class RecipeQueries {
    /**
     * Gets all the recipes for a paginated select.
     * @param server database
     * @param start starting index
     * @param size amount to return
     * @return the recipes in the range [start, start+size]
     */
    public static Result<ArrayList<entities.Recipe>> getRecipesRange(
        ServerDB server, int start, int size) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT RECIPE.recipeID as recipeID, RECIPE.name as NAME, RECIPE.URL as url, " +
                    "Avg(ADDS.rating) as rating, Sum(ADDS.timesCooked) as timesCooked, " +
                    "Max(ADDS.lastCooked) as lastCooked FROM " +
                    "RECIPE LEFT JOIN ADDS ON ADDS.recipeID=RECIPE.recipeID " +
                    "GROUP BY RECIPE.recipeID, Recipe.name, recipe.url LIMIT ?,?;");

            stat.setInt(1, start);
            stat.setInt(2, size);
            result = stat.executeQuery();

            ArrayList<Recipe> recipes = ResultSetParser.parseAugmentedRecipes(result);
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

    /**
     * Gets the recipe master list
     * @param server database
     * @return all recipes in the system.
     */
    public static Result<ArrayList<Recipe>> getAllRecipes(ServerDB server) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;

        try {
            stat = conn.prepareStatement(
                "select * FROM RECIPE;");
            result = stat.executeQuery();

            ArrayList<Recipe> recipes = ResultSetParser.parseRecipes(result);
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

    /**
     * Get's all the recipes a user has added.
     * @param server database
     * @param user the user
     * @return recipes added, or error
     */
    public static Result<ArrayList<Adds>> getAddsRecipe(ServerDB server, User user) {
        Result<ArrayList<Recipe>> allRecipes = getAllRecipes(server);
        if (allRecipes.isSuccess()) {
            Connection conn = server.getConnection();
            PreparedStatement stat = null;
            ResultSet result = null;
            try {
                ArrayList<Adds> adds = new ArrayList<>();
                for (Recipe recipe : allRecipes.value()) {
                    stat = conn.prepareStatement(
                        "select * \n" +
                            "FROM ADDS " +
                            "WHERE ADDS.userID = ? " +
                            "AND ADDS.recipeID = ?;");

                    stat.setString(1, user.getUserId());
                    stat.setString(2, recipe.getRecipeId());
                    result = stat.executeQuery();

                    if (result.next()) {
                        adds.add(ResultSetParser.parseAdds(result));
                    }
                }
                return Result.success(adds);
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
        }

        return Result.failure("There was an error processing your request. " +
            "Please contact software developer with the previous output");

    }

    /**
     * Adds a recipe to the database along with the ingredients
     * @param server database
     * @param recipe to insert
     * @param lists to associate
     * @return success if successful, error if not
     */
    public static Result addRecipe(ServerDB server, Recipe recipe, ArrayList<Lists> lists) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;

        try {
            stat = conn.prepareStatement(
                "INSERT INTO RECIPE VALUES(?, ?, ?)");
            stat.setString(1, recipe.getRecipeId());
            stat.setString(2, recipe.getName());
            stat.setString(3, recipe.getUrl());
            stat.executeUpdate();

            for (int i = 0; i < lists.size(); i++) {
                stat = conn.prepareStatement(
                    "INSERT INTO LISTS VALUES(?, ?, ?)");
                stat.setString(1, recipe.getRecipeId());
                stat.setString(2, lists.get(i).ingredientID);
                stat.setBoolean(3, lists.get(i).isRequired);
                stat.executeUpdate();
            }

            System.out.println("Recipe: " + recipe.getName() + " inserted into the database");
            return Result.success(null);
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

    /**
     * Adds a recipe to a user's cart
     * @param server database
     * @param user to add to
     * @param recipe to add
     * @return success if successful, error if not.
     */
    public static Result addRecipeCart(ServerDB server, User user, Recipe recipe) {

        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;

        //if the recipe is already in ADDS (because we have already reveiewed it once before) then we need
        //to an update not an insert
        try {

            Result<ArrayList<Adds>> adds = getAddsRecipe(server, user);
            if (adds.isSuccess()) {
                if (!Adds.contains(recipe, adds.value())) {
                    stat = conn.prepareStatement("INSERT INTO ADDS VALUES(?, ?, null, 0, null)");
                    stat.setString(1, user.getUserId());
                    stat.setString(2, recipe.getRecipeId());

                    stat.executeUpdate();
                    return Result.success(null);
                } else {
                    return Result.failure("User already has recipe.");
                }
            }
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

    /**
     * Deletes a recipe from the database
     * @param server database
     * @param recipe to remove
     * @return success if successful, error if not
     */
    public static Result deleteRecipe(ServerDB server, Recipe recipe) {

        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;

        try {

            stat = conn.prepareStatement("DELETE FROM LISTS WHERE recipeID=?;");
            stat.setString(1, recipe.getRecipeId());
            stat.executeUpdate();

            stat = conn.prepareStatement("DELETE FROM ADDS WHERE recipeID=?;");
            stat.setString(1, recipe.getRecipeId());
            stat.executeUpdate();

            stat = conn.prepareStatement("DELETE FROM RECIPE WHERE recipeID=?;");
            stat.setString(1, recipe.getRecipeId());
            stat.executeUpdate();

            return Result.success(null);
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

    //method to update the rating of a recipe
    public static Result updateRecipe(ServerDB server, User user, Recipe recipe, float rating) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        try {
            stat = conn.prepareStatement(
                "UPDATE ADDS " +
                    "SET ADDS.lastCooked=?,ADDS.timesCooked = ?, ADDs.rating=? " +
                    "WHERE ADDS.recipeID=? " +
                    "AND ADDS.userID=?;");

            stat.setString(1, currentTime);
            stat.setInt(2, recipe.getTimesCooked() + 1);
            stat.setFloat(3, rating);

            stat.setString(4, recipe.getRecipeId());
            stat.setString(5, user.getUserId());
            stat.executeUpdate();

            return Result.success(null);
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
        return Result.failure("There was an error processing your request.");
    }

    public static Result<ArrayList<entities.Recipe>> getRecipesRating(
        ServerDB server, int start, int size, double rate) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT RECIPE.recipeID as recipeID, RECIPE.name as name,  " +
                    "RECIPE.URL as url, Avg(ADDS.rating) as rating, Sum(ADDS.timesCooked) as timesCooked, " +
                    "Max(ADDS.lastCooked) as lastCooked FROM RECIPE, ADDS " +
                    "WHERE ADDS.recipeID=RECIPE.recipeID AND rating > ? " +
                    "GROUP BY RECIPE.recipeID,  RECIPE.name,  RECIPE.url" +
                    " LIMIT ?, ?;");

            stat.setDouble(1, rate);
            stat.setInt(2, start);
            stat.setInt(3, size);
            String query = stat.toString();
            result = stat.executeQuery();

            ArrayList<Recipe> recipes = ResultSetParser.parseAugmentedRecipes(result);
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

    public static Result<ArrayList<entities.Recipe>> getRecipesCooked(
        ServerDB server, int start, int size, double rate) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT RECIPE.recipeID as recipeID, RECIPE.name as name,  " +
                    "RECIPE.URL as url, Avg(ADDS.rating) as rating, ADDS.timesCooked, ADDS.lastCooked FROM RECIPE, ADDS " +
                    "WHERE ADDS.recipeID=RECIPE.recipeID AND ADDS.timesCooked > ? GROUP BY RECIPE.recipeID LIMIT ?, ?;");

            stat.setDouble(1, rate);
            stat.setInt(2, start);
            stat.setInt(3, size);

            result = stat.executeQuery();

            ArrayList<Recipe> recipes = ResultSetParser.parseAugmentedRecipes(result);
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
