package db.queries;

import db.ServerDB;
import entities.Ingredient;
import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.Result;

/**
 * Queries regarding a user's cart.
 */
public class CartQueries {

    /**
     * Retrieves cart of the user from the database.
     *
     * @param server	database
     * @param user		whose cart to get
     * @return cart if successful, error if not
     */
    public static Result<entities.Cart> getCart(ServerDB server, User user) {
        // set up connection
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            // create query to pull the cart
            stat = conn.prepareStatement(
                "SELECT CONTAINS.ingredientID as ingredientID, INGREDIENT.name as name\n" +
                    "FROM USER, STORES, INGREDIENT, CONTAINS\n" +
                    "WHERE USER.userID = ?" +
                    "AND USER.cartID = ?" +
                    "AND CONTAINS.cartID = USER.cartID " +
                    "AND CONTAINS.ingredientID = INGREDIENT.ingredientID " +
                    "Group BY contains.ingredientID;");

            stat.setString(1, user.getUserId());
            stat.setString(2, user.getCartId());

            // execute query
            result = stat.executeQuery();

            // build result list
            ArrayList<Ingredient> ingredients = ResultSetParser.parseIngredients(result);

            // build cart
            return Result.success(new entities.Cart(user.getCartId(), ingredients));
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
