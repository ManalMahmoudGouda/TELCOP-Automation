package database;

import org.json.simple.JSONObject;
import org.testng.Assert;
import util.JDBCAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateCourseDB {

    public Integer getMaxLimitOfNewCourseConfiguration(JSONObject jsonConfig, String configID) throws SQLException, ClassNotFoundException {
        JDBCAdapter jdbc = new JDBCAdapter();
        Connection connection = jdbc.getSysConfigConnection(jsonConfig);

        final String SQL = "SELECT param_value FROM config_parameter WHERE id = " + configID;

        Statement stm = connection.createStatement();
        ResultSet resultSet = stm.executeQuery(SQL);

        Integer maxLimit = null;
        if(resultSet.first()){
            String value = resultSet.getString("param_value");
            Assert.assertNotNull(value);
            Assert.assertTrue(value.trim().matches("[0-9]+"), "Value isn't number");

            maxLimit = Integer.parseInt(value);
            Assert.assertTrue(maxLimit > 0, "Max Limit Configuration is less than or equal Zero");
        } else
            Assert.fail("Max Limit Configuration doesn't exist in Database");

        resultSet.close();
        stm.close();
        connection.close();

        return maxLimit;
    }

    public Integer countOfUnCompletedCourses(JSONObject jsonConfig, String userID) throws SQLException, ClassNotFoundException {
        JDBCAdapter jdbc = new JDBCAdapter();
        Connection connection = jdbc.getTelcopDorrarConnection(jsonConfig);

        final String SQL = "SELECT COUNT(ID) AS cor_count FROM req_course " +
                "WHERE is_done = 0 AND instructor_id = " + userID;

        Statement stm = connection.createStatement();
        ResultSet resultSet = stm.executeQuery(SQL);

        resultSet.first();
        Integer count = Integer.parseInt(resultSet.getString("cor_count"));

        resultSet.close();
        stm.close();
        connection.close();

        return count;
    }

}
