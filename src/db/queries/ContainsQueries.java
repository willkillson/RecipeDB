package db.queries;

import db.ServerDB;
import entities.Lists;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ContainsQueries {

    public static void updateContains(ServerDB server, User user, ArrayList<ArrayList<Lists>> lists){
        Connection conn = server.getConnection();
        PreparedStatement stat = null;
        ResultSet result = null;
        try {

            for(int i = 0;i< lists.size();i++) {
                for(int j = 0;j< lists.get(i).size();j++) {

                    stat = conn.prepareStatement("insert into CONTAINS " +
                            "values(?,?);");
                    stat.setString(1, user.getCartId());
                    stat.setString(2, lists.get(i).get(j).ingredientID);
                    stat.executeUpdate();
                }
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
