package db.queries;

import db.ServerDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
