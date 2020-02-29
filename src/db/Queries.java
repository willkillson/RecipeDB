package db;

import entities.Cart;
import entities.Cupboard;
import entities.Ingredient;
import entities.Recipe;
import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import util.Result;

public class Queries {

    public static boolean checkConnection(ServerDB server) throws SQLException {
        Connection conn = server.getConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeQuery("SELECT 1 FROM USER");
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return true;
    }

    /**
     * Retrieve user from database and verify password
     *
     * @return user if successful, error if not
     */
    public static Result<User> verifyUser(ServerDB server,
                                          String username,
                                          String password) {

        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT userID, cupboardID, cartID\n" +
                    "FROM USER\n" +
                    "WHERE USER.userID=?\n" +
                    "AND USER.password = ?;");
            stat.setString(1, username);
            stat.setString(2, password);

            result = stat.executeQuery();

            if (!result.next()) {
                return Result.failure("Error: invalid username/password");
            }
            String userId = result.getString("userID");
            String email = result.getString("cupboardID");
            String cupboardID = result.getString("cupboardID");
            String cartID = result.getString("cartID");

            return Result.success(new User(userId, email, cupboardID, cartID));
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
     * Retrieves cupboard of the user from the database.
     *
     * @return cupboard if successful, error if not
     */
    public static Result<Cupboard> getCupboard(ServerDB server, User user) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT STORES.ingredientID as ingredientID, INGREDIENT.name as name\n" +
                    "FROM USER, STORES, INGREDIENT\n" +
                    "WHERE USER.userID = ?" +
                    "AND USER.cupboardID = ?" +
                    "AND STORES.cupboardID = USER.cupboardID " +
                    "AND STORES.ingredientID = INGREDIENT.ingredientID;");

            stat.setString(1, user.getUserId());
            stat.setString(2, user.getCupboardId());

            result = stat.executeQuery();

            ArrayList<Ingredient> ingredients = new ArrayList<>();
            while (result.next()) {
                String ingredientID = result.getString("ingredientID");
                String name = result.getString("name");
                ingredients.add(new Ingredient(ingredientID, name));
            }
            return Result.success(new Cupboard(user.getCupboardId(), ingredients));
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
     * Retrieves cart of the user from the database.
     *
     * @return cart if successful, error if not
     */
    public static Result<Cart> getCart(ServerDB server, User user) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement(
                "SELECT CONTAINS.ingredientID as ingredientID, INGREDIENT.name as name\n" +
                    "FROM USER, STORES, INGREDIENT\n" +
                    "WHERE USER.userID = ?" +
                    "AND USER.cartID = ?" +
                    "AND CONTAINS.cartID = USER.cartID " +
                    "AND CONTAINS.ingredientID = INGREDIENT.ingredientID;");

            stat.setString(1, user.getUserId());
            stat.setString(2, user.getCartId());

            result = stat.executeQuery();

            ArrayList<Ingredient> ingredients = new ArrayList<>();
            while (result.next()) {
                String ingredientID = result.getString("ingredientID");
                String name = result.getString("name");
                ingredients.add(new Ingredient(ingredientID, name));
            }
            return Result.success(new Cart(user.getCartId(), ingredients));
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

    public static Result<ArrayList<Recipe>> getRecipes(ServerDB server, int start, int stop) {
        return Result.failure("Not Implemented");
    }

//
//
//
//    void listRecipes(){
//
//        /*
//
//            select *
//            from recipes;
//
//         */
//
//    }
//
//    void displayLocalIngredients(){
//
//    }
//
//    void addRecipes(){
//
//
//
//    }
//
//    void removeRecipe(){}
//
//    void getShoppingList(){
//
//    }
//
//    void getAvailableRecipes(){
//
//         //TODO
//
//    }
}
