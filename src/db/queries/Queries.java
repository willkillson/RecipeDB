package db.queries;

import db.ServerDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Queries {
    /**
     * Utility function to check for a connection being established.
     * @param server to check
     * @return true if successful
     * @throws SQLException in the event of an error
     */
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
}
