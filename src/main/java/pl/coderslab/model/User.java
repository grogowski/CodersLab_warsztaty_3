package pl.coderslab.model;



import pl.coderslab.utils.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private UserGroup userGroup;

    public User() {
    }

    public User(String username, String email, String password, UserGroup userGroup) {
        this.username = username;
        this.email = email;
        setPassword(password);
        this.userGroup = userGroup;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    private void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    static public User[] loadAllUsers(Connection connection) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        String sql = "SELECT * FROM users";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            users.add(getUserFromResultSet(resultSet, connection));
        }
        User[] uArray = new User[users.size()];
        uArray = users.toArray(uArray);
        return uArray;
    }

    public static User[] loadAllUsersByGroupId(Connection connection, int groupId) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        String sql = "SELECT * FROM users where user_group_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, groupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            users.add(getUserFromResultSet(resultSet, connection));
        }
        User[] uArray = new User[users.size()];
        uArray = users.toArray(uArray);
        return uArray;
    }

    static public User loadUserById(Connection connection, int id) throws SQLException {
        String sql = "SELECT * FROM users where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return getUserFromResultSet(resultSet, connection);
        }
        return null;
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            String sql = "INSERT INTO users(username, email, password, user_group_id) VALUES (?, ?, ?, ?)";
            String[] generatedColumns = {"ID"};
            PreparedStatement preparedStatement = connection.prepareStatement(sql, generatedColumns);
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.setInt(4, this.userGroup.getId());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE users SET username=?, email=?, password=?, user_group_id=? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.setInt(4, this.userGroup.getId());
            preparedStatement.setInt(5, this.id);
            preparedStatement.executeUpdate();
        }
    }

    private static User getUserFromResultSet(ResultSet resultSet, Connection connection) throws SQLException {
        User loadedUser = new User();
        loadedUser.id = resultSet.getInt("id");
        loadedUser.username = resultSet.getString("username");
        loadedUser.password = resultSet.getString("password");
        loadedUser.email = resultSet.getString("email");
        loadedUser.userGroup = UserGroup.loadUserGroupById(connection, resultSet.getInt("user_group_id"));
        return loadedUser;
    }

    @Override
    public String toString() {
        return "User " + id +
                ": username = " + username +
                ", password = " + password +
                ", email = " + email;
    }
}
