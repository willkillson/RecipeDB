import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class ServerDB {

    //Resources
    public ResultSet rs;
    public PreparedStatement ps;
    public Statement stmt;
    public Connection conn;

    public String dbName;
    public String url;
    public String driver;
    public String adminName;
    public String adminPass;
    public String ip;


    /**
     *         "jdbc:mysql://192.168.1.2/jdbclab?autoReconnect=true&useSSL=false"
     *         kevin
     *         6016
     * @param ip
     * @param driver
     * @param dbName
     * @param adminName
     * @param dbpw
     */
    public ServerDB(String ip, String driver,String dbName,  String adminName, String dbpw){
        rs = null;
        ps = null;
        stmt = null;
        conn = null;

        this.ip = ip;
        this.driver = driver;
        this.dbName = dbName;
        this.url = "jdbc:mysql://"+ip+"/"+dbName+"?autoReconnect=true&useSSL=false";
        this.adminName = adminName;
        this.adminPass = dbpw;
    }

    public void connect() throws ClassNotFoundException, SQLException {

        Class.forName(this.driver);
        conn = DriverManager.getConnection(url,this.adminName,this.adminPass);
    }

    public void close() throws SQLException {
        if (rs != null)
            rs.close();
        if (stmt != null)
            stmt.close();
        if (ps != null)
            ps.close();
        if (conn != null)
            conn.close();
    }



}
