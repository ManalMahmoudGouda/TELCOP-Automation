package util;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCAdapter {

    private Connection createConnection(JSONObject config) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        String jdbcURL = (String) config.get("jdbcURL");
        String username = (String) config.get("username");
        String password = (String) config.get("password");

        return DriverManager.getConnection(jdbcURL,username,password);

    }

    public Connection getUserIdentityConnection(JSONObject config) throws ClassNotFoundException, SQLException {
        JSONObject database = (JSONObject) config.get("database");
        JSONObject dbConfig = (JSONObject) database.get("userIdentity");

        return this.createConnection(dbConfig);
    }

    public Connection getSysConfigConnection(JSONObject config) throws ClassNotFoundException, SQLException {
        JSONObject database = (JSONObject) config.get("database");
        JSONObject dbConfig = (JSONObject) database.get("sysConfig");

        return this.createConnection(dbConfig);
    }

    public Connection getTelcopDorrarConnection(JSONObject config) throws ClassNotFoundException, SQLException {
        JSONObject database = (JSONObject) config.get("database");
        JSONObject dbConfig = (JSONObject) database.get("telcopDorrar");

        return this.createConnection(dbConfig);
    }
}
