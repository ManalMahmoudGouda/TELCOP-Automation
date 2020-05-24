package util;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCAdapter {
    private Connection userIDConnection;
    private Connection sysConfigConnection;
    private Connection dorrarConnection;

    public JDBCAdapter(JSONObject config) throws SQLException, ClassNotFoundException {
        this.userIDConnection = this.createUserIDConnection(config);
        this.sysConfigConnection = this.createSysConfigConnection(config);
        this.dorrarConnection = this.createDorrarConnection(config);
    }

    private Connection createConnection(JSONObject config) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        String jdbcURL = (String) config.get("jdbcURL");
        String username = (String) config.get("username");
        String password = (String) config.get("password");

        return DriverManager.getConnection(jdbcURL,username,password);

    }

    private Connection createUserIDConnection(JSONObject config) throws ClassNotFoundException, SQLException {
        JSONObject database = (JSONObject) config.get("database");
        JSONObject dbConfig = (JSONObject) database.get("userIdentity");

        return this.createConnection(dbConfig);
    }

    private Connection createSysConfigConnection(JSONObject config) throws ClassNotFoundException, SQLException {
        JSONObject database = (JSONObject) config.get("database");
        JSONObject dbConfig = (JSONObject) database.get("sysConfig");

        return this.createConnection(dbConfig);
    }

    private Connection createDorrarConnection(JSONObject config) throws ClassNotFoundException, SQLException {
        JSONObject database = (JSONObject) config.get("database");
        JSONObject dbConfig = (JSONObject) database.get("telcopDorrar");

        return this.createConnection(dbConfig);
    }

    public Connection getUserIDConnection() {
        return userIDConnection;
    }

    public Connection getSysConfigConnection() {
        return sysConfigConnection;
    }

    public Connection getDorrarConnection() {
        return dorrarConnection;
    }
}
