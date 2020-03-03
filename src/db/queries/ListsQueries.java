package db.queries;

import db.ServerDB;
import entities.Lists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListsQueries {
    public static ArrayList<ArrayList<Lists>> getLists(ServerDB server, ArrayList<String> recipeIDs) {
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        ArrayList<ArrayList<Lists>> lists = new ArrayList<>();
        try {

            for(int i = 0;i< recipeIDs.size();i++){
                stat = conn.prepareStatement("SELECT LISTS.recipeID, LISTS.ingredientID, LISTS.isRequired " +
                        "FROM LISTS " +
                        "where LISTS.recipeID =?;");
                stat.setString(1, recipeIDs.get(i));
                result = stat.executeQuery();
                lists.add(ResultSetParser.parseLists(result));
            }

            return lists;
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
        System.out.println("There was an error processing your request. " +
                "Please contact software developer with the previous output");
        return lists;
    }
}
