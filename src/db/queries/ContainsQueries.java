package db.queries;

import db.ServerDB;
import entities.Lists;
import entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.Result;

public class ContainsQueries {
    /**
     * Clears the cart of the selected user
     *
     * @param server to use
     * @param user   whose cart needs clearing.
     */
    public static Result clearContainsRelations(ServerDB server, User user) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            stat = conn.prepareStatement("DELETE FROM CONTAINS WHERE CONTAINS.cartID=?;");
            stat.setString(1, user.getCartId());
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

    /**
     * Updates the shopping cart with a new set of items.
     *
     * @param server the server to use
     * @param user   the user to add to
     * @param lists  of ingredients to add
     */
    public static void insertContainsRelations(ServerDB server, User user,
                                               ArrayList<Lists> lists) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {
            // insert every list relation
            for (Lists list : lists) {
                stat = conn.prepareStatement("insert into CONTAINS " +
                    "values(?,?);");
                stat.setString(1, user.getCartId());
                stat.setString(2, list.ingredientID);
                stat.executeUpdate();
            }

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
}
