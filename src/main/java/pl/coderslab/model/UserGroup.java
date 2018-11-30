package pl.coderslab.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserGroup {

    private int id;
    private String name;

    public UserGroup() {
    }

    public UserGroup(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    static public UserGroup[] loadAllUserGroups(Connection connection) throws SQLException {
        ArrayList<UserGroup> userGroups = new ArrayList<>();
        String sql = "SELECT * FROM user_group";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            UserGroup loadedUserGroup = new UserGroup();
            loadedUserGroup.id = resultSet.getInt("id");
            loadedUserGroup.name = resultSet.getString("name");
            userGroups.add(loadedUserGroup);
        }
        UserGroup[] ugArray = new UserGroup[userGroups.size()];
        ugArray = userGroups.toArray(ugArray);
        return ugArray;
    }

    static public UserGroup loadUserGroupById(Connection connection, int id) throws SQLException {
        String sql = "SELECT * FROM user_group where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            UserGroup loadedUserGroup = new UserGroup();
            loadedUserGroup.id = resultSet.getInt("id");
            loadedUserGroup.name = resultSet.getString("name");
            return loadedUserGroup;
        }
        return null;
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            String deleteGroup = "DELETE FROM user_group WHERE id=?";
            PreparedStatement groupStatement = connection.prepareStatement(deleteGroup);
            groupStatement.setInt(1, this.id);
            groupStatement.executeUpdate();
            this.id = 0;
        }
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            String sql = "INSERT INTO user_group(name) VALUES (?)";
            String[] generatedColumns = {"ID"};
            PreparedStatement preparedStatement = connection.prepareStatement(sql, generatedColumns);
            preparedStatement.setString(1, this.name);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE user_group SET name=? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.name);
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
