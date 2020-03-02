package db.queries;

import db.ServerDB;
import entities.Recipe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import javax.swing.text.html.Option;

import entities.User;
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
                    "RECIPE.URL as url, Avg(ADDS.rating) as rating, ADDS.timesCooked, ADDS.lastCooked FROM RECIPE, ADDS " +
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
                int timesCooked = result.getInt("timesCooked");
                Date lastCooked = result.getDate("lastCooked");
                recipes.add(new Recipe(recipeID, name, Optional.of(rating), url, timesCooked, lastCooked));
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


    /**
     *
     * @param server
     * @param usr
     * @return
     */
    public static Result<ArrayList<entities.Recipe>> getAddsRecipes(ServerDB server, User usr){

        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                    "select RECIPE.recipeID, RECIPE.name, RECIPE.url, \n" +
                            "ADDS.rating, ADDS.timesCooked, ADDS.lastCooked, ADDS.userID\n" +
                            "from adds, recipe\n" +
                            "where adds.recipeID = recipe.recipeID\n" +
                            "AND ADDS.rating IS NULL " +
                            "AND adds.userID=?;");

            stat.setString(1, usr.getUserId());


            result = stat.executeQuery();

            ArrayList<Recipe> recipes = new ArrayList<>();
            while (result.next()) {
                String recipeID = result.getString("recipeID");
                String name = result.getString("name");
                Double rating = result.getDouble("rating");
                String url = result.getString("url");
                int timesCooked = result.getInt("timesCooked");
                Date lastCooked = result.getDate("lastCooked");
                recipes.add(new Recipe(recipeID, name, Optional.of(rating), url, timesCooked, lastCooked));
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



    public static void addRecipe(ServerDB server, Recipe recipe) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        for (Recipe recipe : recipes) {
            if (ID == recipe.getRecipeId()) {
                System.out.println("Recipe with this ID already exists");
                return;
            }
        }
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter a RecipeID for the recipe" );
            String recipeID = in.nextLine();
            System.out.println("Enter a name for the recipe" );
            String name = in.nextLine();
            System.out.println("Enter a URL for the recipe" );
            String URL = in.nextLine();
            stat = conn.prepareStatement(
                    "INSERT INTO RECIPE VALUES(?, ?, ?)");
            stat.setString(1, recipeID);
            stat.setString(2, name);
            stat.setString(3, URL);
            stat.executeUpdate();
            System.out.println("Enter the last cooked date for the recipe ; dd/mm/yyyy" );
            String date = in.nextLine();
            java.util.Date myDate = new java.util.Date(date);
            java.sql.Date lastCooked = new java.sql.Date(myDate.getTime());

            System.out.println("Enter the number of times this recipe was cooked" );
            int timesCooked = in.nextInt();
            System.out.println("Enter a rating for the recipe" );
            int rating = in.nextInt();
            stat = conn.prepareStatement(
                    "INSERT INTO ADDS VALUES(null, ?, ?, ?, ?)");
            stat.setString(1, recipeID);
            stat.setDate(2, lastCooked);
            stat.setInt(3, timesCooked);
            stat.setInt(4, rating);
            stat.executeUpdate();
            System.out.println("Recipe: " + recipeID + " inserted into the database");
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

    public static void addRecipeCart(){

    }


    //if we want to be able to delete a recipe, do not implement.
    /**public static ArrayList<entities.Recipe> deleteRecipe(ServerDB server, String ID, ArrayList<entities.Recipe> recipes) {
        Recipe delete=null;
        for (Recipe recipe : recipes) {
            if (ID == recipe.getRecipeId()) {
                delete = recipe;
                recipes.remove(delete);
            }
        }
        if (delete == null) {
            System.out.println("Recipe with that ID does not exist");
            return recipes;
        }

        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                    "DELETE FROM RECIPE WHERE recipeID=?;");
            stat.setString(1, ID);

            stat.executeUpdate();
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
        System.out.println("There was an error processing your request.");
        return recipes;
    }**/

    //method to update the rating of a recipe
    public static void updateRecipe(ServerDB server, String ID, ArrayList<entities.Recipe> recipes, int rating) {
        Recipe update=null;
        for (Recipe recipe : recipes) {
            if (ID == recipe.getRecipeId()) {
                update = recipe;
                recipe.setRating(rating);
            }
        }
        if (update == null) {
            System.out.println("Recipe with that ID does not exist");
            return;
        }
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;

        try {
            stat = conn.prepareStatement(
                    "UPDATE ADDS SET rating = ? WHERE recipeID=?;");

            stat.setInt(1, rating);
            stat.setString(2, ID);

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

            ArrayList<Recipe> recipes = new ArrayList<>();
            while (result.next()) {
                String recipeID = result.getString("recipeID");
                String name = result.getString("name");
                Double rating = result.getDouble("rating");
                String url = result.getString("url");
                int timesCooked = result.getInt("timesCooked");
                Date lastCooked = result.getDate("lastCooked");
                recipes.add(new Recipe(recipeID, name, Optional.of(rating), url, timesCooked, lastCooked));
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

            ArrayList<Recipe> recipes = new ArrayList<>();
            while (result.next()) {
                String recipeID = result.getString("recipeID");
                String name = result.getString("name");
                Double rating = result.getDouble("rating");
                String url = result.getString("url");
                int timesCooked = result.getInt("timesCooked");
                Date lastCooked = result.getDate("lastCooked");
                recipes.add(new Recipe(recipeID, name, Optional.of(rating), url, timesCooked, lastCooked));
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
