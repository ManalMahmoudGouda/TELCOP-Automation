package database.security;

import org.json.simple.JSONObject;
import pages.RegistrationPage;
import util.JDBCAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginDB {
    private JDBCAdapter jdbc;

    public LoginDB(JSONObject jsonConfig) throws SQLException, ClassNotFoundException {
        this.jdbc = new JDBCAdapter(jsonConfig);
    }

    public Boolean checkIsUserExist(String username) throws SQLException, ClassNotFoundException {
        System.out.println("Before Method");
        Connection con = jdbc.getUserIDConnection();

        String SQL = "SELECT COUNT(id) AS usr_count FROM auth_user WHERE username = '" + username + "'";

        Statement stm = con.createStatement();
        ResultSet resultSet = stm.executeQuery(SQL);

        resultSet.first();
        Integer count = Integer.parseInt(resultSet.getString("usr_count"));

        resultSet.close();
        stm.close();
        con.close();

        return count > 0;
    }
}
