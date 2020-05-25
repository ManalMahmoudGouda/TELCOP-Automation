package database;

import org.json.simple.JSONObject;
import org.testng.Assert;
import util.JDBCAdapter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDB {
    private JDBCAdapter jdbc;

    public UsersDB(JSONObject jsonConfig) throws SQLException, ClassNotFoundException {
        this.jdbc = new JDBCAdapter(jsonConfig);
    }

    public Boolean checkIsUserExist(String username) throws SQLException {
        Connection con = jdbc.getUserIDConnection();

        String SQL = "SELECT COUNT(id) AS usr_count FROM auth_user WHERE username = '" + username + "'";

        Statement stm = con.createStatement();
        ResultSet resultSet = stm.executeQuery(SQL);

        resultSet.first();
        Integer count = Integer.parseInt(resultSet.getString("usr_count"));

        resultSet.close();
        stm.close();

        return count > 0;
    }

    public Integer getUserID(String username) throws SQLException {
        Connection con = jdbc.getUserIDConnection();

        String SELECT_USER_ID = "SELECT id FROM auth_user WHERE username = '" + username + "'";

        Statement stm = con.createStatement();
        ResultSet resultSet = stm.executeQuery(SELECT_USER_ID);

        resultSet.first();
        Integer userID = resultSet.getInt("id");
        Assert.assertNotNull(userID, "No User with Username '" + username + "' in Database");

        resultSet.close();
        stm.close();
        return userID;
    }

    public void grantUserRole(String username, String roleID) throws SQLException, InterruptedException {
        Connection con = jdbc.getUserIDConnection();

        Thread.sleep(3000);
        Integer userID = this.getUserID(username);
        Statement selectStm = con.createStatement();
        Statement insertStm = con.createStatement();

        //Select Role Action Privileges
        ResultSet rs1 = selectStm.executeQuery("SELECT action_id from auth_role_action WHERE role_id = " + roleID);
        while (rs1.next()) {
            Integer actionID = rs1.getInt("action_id");
            try {
                insertStm.executeUpdate("INSERT INTO auth_user_action (user_id, action_id) " +
                        "VALUES (" + userID + ", " + actionID + ")");
            } catch (SQLIntegrityConstraintViolationException ignored){ }
        }
        rs1.close();

        //Select Role View Privileges
        ResultSet rs2 = selectStm.executeQuery("SELECT view_id from auth_role_view WHERE role_id = " + roleID);
        while (rs2.next()) {
            Integer viewID = rs2.getInt("view_id");
            try {
                insertStm.executeUpdate("INSERT INTO auth_user_view (user_id, view_id) " +
                        "VALUES (" + userID + ", " + viewID + ")");
            } catch (SQLIntegrityConstraintViolationException ignored){ }
        }
        rs2.close();

        // Insert Role Privileges
        insertStm.executeUpdate("INSERT INTO auth_user_role (user_id, role_id, is_default) VALUES (" + userID + ", " + roleID + ", 1)");

        selectStm.close();
        insertStm.close();
    }
}
