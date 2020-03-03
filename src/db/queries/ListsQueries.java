package db.queries;

import db.ServerDB;
import entities.Lists;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.Result;

public class ListsQueries {

    /**
     * Retrieves the lists relation for all recipe ids requested
     * @param server database
     * @param recipeIDs to fetch
     * @return a master list of all lists relations, or
     */
    public static Result<ArrayList<Lists>> getLists(ServerDB server, ArrayList<String> recipeIDs) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        ArrayList<Lists> lists = new ArrayList<>();
        try {
            // add each recipe to the LISTS
            for (String recipeID : recipeIDs) {
                stat = conn.prepareStatement("SELECT LISTS.recipeID, LISTS.ingredientID, LISTS.isRequired " +
                    "FROM LISTS " +
                    "where LISTS.recipeID =?;");
                stat.setString(1, recipeID);
                result = stat.executeQuery();

                // aggregate the results
                lists.addAll(ResultSetParser.parseLists(result));
            }

            return Result.success(lists);
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
