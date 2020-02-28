package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import util.Result;


public class ServerDB {
    private Config config;
    private static HashMap<Config, Connection> singletons = new HashMap<>();

    public Connection getConnection() {
        return singletons.get(config);
    }

    public static ServerDB create(Config config) throws ClassNotFoundException, SQLException {
        return new ServerDB(config).connect();
    }

    public void close() throws SQLException {
        try {
            Connection connection = singletons.get(config);
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            singletons.remove(config);
            throw e;
        }
    }

    private ServerDB(Config config) {
        this.config = config;
    }

    private ServerDB connect() throws ClassNotFoundException, SQLException {
        if (!singletons.containsKey(config)) {
            Class.forName(config.driver);
            singletons.put(
                config,
                DriverManager.getConnection(config.getUrl(), config.adminName, config.adminPass)
            );
        }
        return this;
    }

    public static class Config {
        public String dbName;
        public String driver;
        public String adminName;
        public String adminPass;
        public String ip;

        /**
         * Configuration class for the database connection
         * "jdbc:mysql://192.168.1.2/jdbclab?autoReconnect=true&useSSL=false"
         * kevin
         * 6016
         *
         * @param ip        ip address of mysql database
         * @param driver    sql driver
         * @param dbName    database name
         * @param adminName database username
         * @param adminPass database password
         */
        private Config(String ip, String dbName, String driver, String adminName, String adminPass) {
            this.ip = ip;
            this.driver = driver;
            this.dbName = dbName;
            this.adminName = adminName;
            this.adminPass = adminPass;
        }

        public String getUrl() {
            return "jdbc:mysql://" + ip + "/" + dbName + "?autoReconnect=true&useSSL=false";
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + ip.hashCode();
            result = 31 * result + driver.hashCode();
            result = 31 * result + dbName.hashCode();
            result = 31 * result + adminName.hashCode();
            result = 31 * result + adminPass.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Config)) {
                return false;
            }
            Config other = (Config) obj;
            return ip.equals(other.ip)
                && driver.equals(other.driver)
                && dbName.equals(other.dbName)
                && adminName.equals(other.adminName)
                && adminPass.equals(other.adminPass);
        }

        public static Builder create() {
            return new Builder();
        }

        public static class Builder {
            private Config config =
                new Config("", "", "", "", "");

            public Builder setIp(String ip) {
                config.ip = ip;
                return this;
            }

            public Builder setDatabaseName(String databaseName) {
                config.dbName = databaseName;
                return this;
            }

            public Builder setDriver(String driver) {
                config.driver = driver;
                return this;
            }

            public Builder setUsername(String username) {
                config.adminName = username;
                return this;
            }

            public Builder setPassword(String password) {
                config.adminPass = password;
                return this;
            }

            public Result<Config> build() {
                if (config.ip.equals("")) {
                    return Result.failure("Ip is required");
                } else if (config.dbName.equals("")) {
                    return Result.failure("Database name is required");
                } else if (config.driver.equals("")) {
                    return Result.failure("Database driver is required");
                } else if (config.adminName.equals("")) {
                    return Result.failure("Username is required");
                } else {
                    return Result.success(config);
                }
            }
        }
    }
}
