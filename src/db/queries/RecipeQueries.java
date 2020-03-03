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
    public static Result<ArrayList<entities.Recipe>> getRecipes(
        ServerDB server, int start, int size) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT RECIPE.recipeID as recipeID, RECIPE.name as NAME, RECIPE.URL as url, " +
                    "Avg(ADDS.rating) as rating, Sum(ADDS.timesCooked) as timesCooked, Max(ADDS.lastCooked) as lastCooked FROM " +
                    "RECIPE LEFT JOIN ADDS ON ADDS.recipeID=RECIPE.recipeID " +
                    "GROUP BY RECIPE.recipeID, Recipe.name, recipe.url LIMIT ?,?;");

            stat.setInt(1, start);
            stat.setInt(2, size);

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


    public static ArrayList<Recipe> getRecipes(ServerDB server) {


        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            stat = conn.prepareStatement(
                "select * \n" +
                    "FROM RECIPE;");


            result = stat.executeQuery();
            while (result.next()) {
                String rId = result.getString("recipeID");
                String rName = result.getString("name");
                String URL = result.getString("URL");
                Recipe recipe = new Recipe(rId, rName, URL);
                recipes.add(recipe);
            }

            return recipes;
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
        return recipes;
    }

    /**
     * @param server
     * @param usr
     * @return all the recipes that the user hasn't rated on.
     */
    public static ArrayList<Adds> getAddsRecipe(ServerDB server, User usr) {


        ArrayList<Recipe> allRecipes = getRecipes(server);
        ArrayList<Adds> adds = new ArrayList<>();

        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {

            for (int i = 0; i < allRecipes.size(); i++) {
                stat = conn.prepareStatement(
                    "select * \n" +
                        "FROM ADDS " +
                        "WHERE ADDS.userID = ? " +
                        "AND ADDS.recipeID = ?;");

                stat.setString(1, usr.getUserId());
                stat.setString(2, allRecipes.get(i).getRecipeId());
                result = stat.executeQuery();

                if (result.next()) {
                    String userId = result.getString("userID");
                    String recipeID = result.getString("recipeID");
                    Integer timesCooked = result.getInt("timesCooked");
                    Date lastCooked = result.getDate("lastCooked");
                    Double rating = result.getDouble("rating");
                    Adds a = new Adds(userId, recipeID, lastCooked, timesCooked, rating);
                    adds.add(a);
                }


            }
            return adds;
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
        return adds;

    }

    public static void addRecipe(ServerDB server, Recipe recipe, ArrayList<Lists> lists) {
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
            return;
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
        System.out.println("There was an error processing your request.");
        return;
    }

    public static void addRecipeCart(ServerDB server, User user, Recipe recipe) {

        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;


        //if the recipe is already in ADDS (because we have already reveiewed it once before) then we need
        //to an update not an insert
        try {

            ArrayList<Adds> adds = getAddsRecipe(server, user);
            if (!Adds.contains(recipe, adds)) {
                stat = conn.prepareStatement("INSERT INTO ADDS VALUES(?, ?, null, 0, null)");
                stat.setString(1, user.getUserId());
                stat.setString(2, recipe.getRecipeId());

                stat.executeUpdate();
                System.out.println("Recipe: " + recipe.getRecipeId() + " added to " + user.getUserId() + "'s recipe cart");
            } else {
                System.out.println("User already has recipe.");
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


        return;
    }

    public static void deleteRecipe(ServerDB server, Recipe recipe) {

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
            return;
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
        System.out.println("There was an error processing your request.");
    }

    //method to update the rating of a recipe
    public static void updateRecipe(ServerDB server, User user, Recipe recipe, float rating) {
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


            return;
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
        System.out.println("There was an error processing your request.");
        return;
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
}
