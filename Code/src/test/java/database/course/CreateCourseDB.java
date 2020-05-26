package database.course;

import org.json.simple.JSONObject;
import org.testng.Assert;
import util.JDBCAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateCourseDB {
    private JDBCAdapter jdbc;
    
    public CreateCourseDB(JSONObject jsonConfig) throws SQLException, ClassNotFoundException {
        this.jdbc = new JDBCAdapter(jsonConfig);
    }

    public Integer getMaxLimitOfNewCourseConfiguration(String configID) throws SQLException, ClassNotFoundException {
        Connection connection = jdbc.getSysConfigConnection();

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

        return maxLimit;
    }

    public Integer countOfUnCompletedCourses(String instructorID) throws SQLException, ClassNotFoundException {
        Connection connection = jdbc.getDorrarConnection();

        final String SQL = "SELECT COUNT(ID) AS cor_count FROM req_course " +
                "WHERE is_course_completed = 0 AND instructor_id = " + instructorID;

        Statement stm = connection.createStatement();
        ResultSet resultSet = stm.executeQuery(SQL);

        resultSet.first();
        Integer count = Integer.parseInt(resultSet.getString("cor_count"));

        resultSet.close();
        stm.close();

        return count;
    }

    public Boolean isCourseExistByCourseTitle(String courseTitle, String instructorID) throws SQLException {
        Connection connection = jdbc.getDorrarConnection();

        final String SQL = "SELECT COUNT(ID) AS cor_count FROM req_course " +
                "WHERE cor_title = " + courseTitle + " AND instructor_id = " + instructorID;

        Statement stm = connection.createStatement();
        ResultSet resultSet = stm.executeQuery(SQL);

        resultSet.first();
        Integer count = Integer.parseInt(resultSet.getString("cor_count"));

        resultSet.close();
        stm.close();
        
        return count>0;
    }

    public String selectUsernameWithCanCreateCourse(String translatorRoleID, String maxNewCourseLimitConfigID) throws SQLException, ClassNotFoundException {
        Connection userIDConnection = jdbc.getUserIDConnection();

        final String INSTRUCTOR_SQL = "SELECT usr.id, usr.username " +
                "FROM auth_user_role aur " +
                    "LEFT JOIN auth_user usr ON aur.user_id = usr.id " +
                "WHERE aur.role_id = " + translatorRoleID;

        Statement userIDStm = userIDConnection.createStatement();
        ResultSet userIDResultSet = userIDStm.executeQuery(INSTRUCTOR_SQL);

        while(userIDResultSet.next()) {
            String userID = userIDResultSet.getString("id");
            String username = userIDResultSet.getString("username");

            Integer maxNewCourseLimit = this.getMaxLimitOfNewCourseConfiguration(maxNewCourseLimitConfigID);

            Integer numOfUncompletedCourses = this.countOfUnCompletedCourses(userID);
            if(maxNewCourseLimit > numOfUncompletedCourses) {
                userIDResultSet.close();
                userIDStm.close();
                return username;
            }
        }
        userIDResultSet.close();
        userIDStm.close();

        return null;
    }
}
