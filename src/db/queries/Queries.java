package db.queries;

import db.ServerDB;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Miscellaneous query for connecting in general.
 */
public class Queries {
	
    /**
     * Utility function to check for a connection being established.
     * 
     * @param server database
     * @return true if successful
     * @throws SQLException in the event of an error
     */
    public static boolean checkConnection(ServerDB server) throws SQLException {
        // set up connection
    	Connection conn = server.getConnection();
        Statement stmt = null;
        try {
        	// create query for checking
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
