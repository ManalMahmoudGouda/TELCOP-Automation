package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCAdapter {
    private Connection con;

    public void initConnection() throws SQLException, ClassNotFoundException {
        String jdbcURL = "jdbc:mysql://localhost:3306/ams_user_id";
        String username = "root";
        String password = "root";
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(jdbcURL,username,password);
    }

    public Connection getCon() {
        return con;
    }
}
